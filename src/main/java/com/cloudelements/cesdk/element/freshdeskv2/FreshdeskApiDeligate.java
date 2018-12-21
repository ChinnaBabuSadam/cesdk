package com.cloudelements.cesdk.element.freshdeskv2;

import com.cloudelements.cesdk.framework.AbstractElementService;
import com.cloudelements.cesdk.service.exception.ServiceException;
import com.cloudelements.cesdk.util.JacksonJsonUtil;
import com.cloudelements.cesdk.util.ServiceConstants;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreshdeskApiDeligate extends AbstractElementService {

    private static final String BASE_URL = "https://cloudelements.freshdesk.com/api/v2";
    private static final String PERIOD_JSON = ".json";

    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void init() {

    }

    @Override
    public Map doBulkDownLoad() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public Map doBulkUpload() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public boolean provision() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public boolean deleteProvision() {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public List<Map> find(Object elementQuery, Object... args) {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public List<Map> find(Object elementQuery) {
        throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The API not yet implemented");
    }

    @Override
    public List<Map> find(String objectName, Map<String, Object> query) {

        if (StringUtils.isBlank(objectName)) {
            throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The resource not yet implimented");
        }

        List<Map> responseList = null;

        if (query == null) {
            query = new HashMap<>();
        }

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(objectName);
        urlBuilder.append(PERIOD_JSON);

        HttpResponse vendorResponse = null;
        try {
            vendorResponse = Unirest.get(urlBuilder.toString()).headers(getHeaders()).queryString(query).asBinary();
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ux.getMessage());
        }

        handleErrorResponse(vendorResponse);
        String responseString = null;
        try {
            responseString = IOUtils.toString(vendorResponse.getRawBody());
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve data from service provider");
        }
        responseList = JacksonJsonUtil.convertStringToList(responseString);

        return responseList;
    }

    @Override
    public List<String> findObjects() {
        return Arrays.asList("companies", "agents", "contacts", "tickets", "ticketsConversations", "surveys",
                "products", "business_hours", "sla_policies", "groups", "roles");
    }

    @Override
    public Map retrieve(String objectName, String id) {
        if (StringUtils.isBlank(objectName) || StringUtils.isBlank(id)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "To retrieve an object you must provide resource and id");
        }
        Map response = new HashMap();
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(objectName);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(id);

        HttpResponse vendorResponse = null;
        try {
            vendorResponse = Unirest.get(urlBuilder.toString()).headers(getHeaders()).asBinary();
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ux.getMessage());
        }

        handleErrorResponse(vendorResponse);

        String responseString = null;
        try {
            responseString = IOUtils.toString(vendorResponse.getRawBody());
            response = JacksonJsonUtil.convertStringToMap(responseString);
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve data from service provider");
        }
        return response;
    }

    @Override
    public Map create(String objectName, Map object) {
        if (StringUtils.isBlank(objectName) || object == null || object.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "To create an object you must provide resource and payload");
        }
        Map response = new HashMap();
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(objectName);

        HttpResponse vendorResponse = null;
        try {
            String body = JacksonJsonUtil.convertMapToString(object);
            vendorResponse =
                    Unirest.post(urlBuilder.toString()).headers(getHeaders()).body(body).asBinary();
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ux.getMessage());
        }

        handleErrorResponse(vendorResponse);

        String responseString = null;
        try {
            responseString = IOUtils.toString(vendorResponse.getRawBody());
            response = JacksonJsonUtil.convertStringToMap(responseString);
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve data from service provider");
        }
        return response;
    }

    @Override
    public Map update(String objectName, String id, Map object) {
        if (StringUtils.isBlank(objectName) || StringUtils.isBlank(id) || object == null || object.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "To update an object you must provide resource, payload and pathParameters");
        }
        Map response = new HashMap();
        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(objectName);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(id);


        HttpResponse vendorResponse = null;
        try {
            String body = JacksonJsonUtil.convertMapToString(object);
            vendorResponse =
                    Unirest.put(urlBuilder.toString()).headers(getHeaders()).body(body).asBinary();
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ux.getMessage());
        }

        handleErrorResponse(vendorResponse);

        String responseString = null;
        try {
            responseString = IOUtils.toString(vendorResponse.getRawBody());
            response = JacksonJsonUtil.convertStringToMap(responseString);
        } catch (IOException e) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to retrieve data from service provider");
        }
        return response;
    }

    @Override
    public void delete(String objectName, String id) {

        if (StringUtils.isBlank(objectName) || StringUtils.isBlank(id)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "To delete an object you must provide resource and id");
        }

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(objectName);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(id);

        HttpResponse vendorResponse = null;
        try {
            vendorResponse =
                    Unirest.delete(urlBuilder.toString()).headers(getHeaders()).asBinary();
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ux.getMessage());
        }

        handleErrorResponse(vendorResponse);
    }

    private void handleErrorResponse(HttpResponse httpResponse) {
        if (httpResponse == null) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
        }

        if (isErrorResponse(httpResponse)) {
            String errorMessage = fetchErrorResponseFromStream(httpResponse);
            if (StringUtils.isBlank(errorMessage)) {
                errorMessage = httpResponse.getStatusText();
            }
            throw new ServiceException(HttpStatus.valueOf(httpResponse.getStatus()), errorMessage);
        }
    }

    private boolean isErrorResponse(HttpResponse httpResponse) {
        int statusCode = httpResponse.getStatus();
        return !(statusCode >= 200 && statusCode <= 207);
    }

    private static <T> String fetchErrorResponseFromStream(HttpResponse<T> httpResponse) {
        try (BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(new
                BufferedInputStream(httpResponse.getRawBody())))) {
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
