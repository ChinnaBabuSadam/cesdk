package com.cloudelements.cesdk.element.bamboohr;

import com.cloudelements.cesdk.element.freshdeskv2.FreshdeskApiDeligate;
import com.cloudelements.cesdk.framework.AbstractElementService;
import com.cloudelements.cesdk.service.domain.BrokerConfig;
import com.cloudelements.cesdk.service.domain.BrokerExpression;
import com.cloudelements.cesdk.service.exception.ServiceException;
import com.cloudelements.cesdk.util.JacksonJsonUtil;
import com.cloudelements.cesdk.util.ServiceConstants;
import com.google.common.collect.ImmutableMap;
import com.jayway.jsonpath.JsonPath;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.MultipartBody;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BambooHRApiDeligate extends AbstractElementService {

    private static final String BASE_URL = "https://api.bamboohr.com/api/gateway.php/sdkbroker2/v1";
    private static final String RESOURCE_METADATA_DIR = "metadata";
    private Map<String, String> headers;

    public Map<String, String> getHeaders() {
        return headers;
    }

    private static final Map<String, String> VENDOR_RESOURCE_MAPPING =
            Collections.unmodifiableMap(ImmutableMap.<String, String>builder()
                    .put("employees", "/employees/directory")
                    .put("files", "/employees/{id}/files")
                    .put("categories", "/files/view")
                    .build());

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void init() {

    }

    @Override
    public List<BrokerConfig> refresh() {
        return null;
    }

    @Override
    public List<Map> fetchSchema() {

        InputStream inputStream =
                FreshdeskApiDeligate.class.getClassLoader().
                        getResourceAsStream(RESOURCE_METADATA_DIR + ServiceConstants.SLASH + "bamboohr.json");

        if (inputStream == null) {
            return null;
        }
        try {
            return JacksonJsonUtil.convertStringToList(IOUtils.toString(inputStream));
        } catch (Exception io) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Couldn't find the metadata for the element freshdeskv2");
        }

    }

    @Override
    public List<Map> find(Object elementQuery, Object... args) {
        return null;
    }

    @Override
    public List<Map> find(String objectName, Map<String, Object> query) {

        String vendorResource = VENDOR_RESOURCE_MAPPING.get(objectName);
        if (StringUtils.isBlank(vendorResource)) {
            throw new ServiceException(HttpStatus.NOT_IMPLEMENTED, "The resource not yet implemented");
        }

        List<Map> responseList = null;

        if (query == null) {
            query = new HashMap<>();
        }

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(vendorResource);

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
        if (isXml(responseString)) {
            JSONObject jsonObject = XML.toJSONObject(responseString);
            if (StringUtils.equalsIgnoreCase(objectName, "categories") && jsonObject != null && !jsonObject.isEmpty()) {
                Object categories = JsonPath.read(String.valueOf(jsonObject), "files.category");
                responseList = JacksonJsonUtil.convertStringToList(JacksonJsonUtil.convertObjectToString(categories));
            }
        } else {
            responseList = JacksonJsonUtil.
                    convertStringToList(JacksonJsonUtil.
                            convertObjectToString(JsonPath.read(responseString, objectName)));
        }

        return responseList;
    }

    private boolean isXml(String responseString) {
        return StringUtils.startsWith(responseString, "<?xml");
    }

    @Override
    public List<String> findObjects() {
        return Arrays.asList("employees", "files");
    }

    @Override
    public Map doFileUpload(String objectName, Object object) {
        return null;
    }

    @Override
    public Map create(String objectName, Map object) {
        if (StringUtils.isBlank(objectName) || object == null || object.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "To create an object you must provide resource and payload");
        }

        if (StringUtils.equalsIgnoreCase(objectName, "files")) {
            return doFileUpload(objectName, object);
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
    public List<Map> find(String objectName, List<BrokerExpression> brokerExpressions) {
        throw new ServiceException(HttpStatus.BAD_REQUEST,
                "To retrieve an object you must provide resource and id");
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
    public com.cloudelements.cesdk.util.HttpResponse retrieveFile(String objectName, String fileId) {

        StringBuilder urlBuilder = new StringBuilder(BASE_URL);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(objectName);
        urlBuilder.append(ServiceConstants.SLASH);
        urlBuilder.append(fileId);

        HttpResponse vendorResponse = null;
        try {
            vendorResponse = Unirest.get(urlBuilder.toString()).headers(getHeaders()).asBinary();
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.INTERNAL_SERVER_ERROR, ux.getMessage());
        }

        handleErrorResponse(vendorResponse);

        com.cloudelements.cesdk.util.HttpResponse httpResponse = new com.cloudelements.cesdk.util.HttpResponse();

        httpResponse.setHeaders(vendorResponse.getHeaders());
        httpResponse.setRawBody(vendorResponse.getRawBody());

        return httpResponse;
    }

    public com.cloudelements.cesdk.util.HttpResponse postFile(String objectName, List<FileItem> fileItems, Map<String
            , Object> multipartFormBody, Map<String
            , Object> reqHeaders) {

        HttpRequestWithBody request = null;
        HttpResponse response = null;
        MultipartBody multipartBody = null;
        try {
            StringBuilder urlBuilder = new StringBuilder(BASE_URL);
            urlBuilder.append(ServiceConstants.SLASH);
            urlBuilder.append(objectName);

            Map<String, String> headers = new HashMap<>();
            for (String key : reqHeaders.keySet()) {
                headers.put(key, String.valueOf(reqHeaders.get(key)));
            }
            //            headers.remove("content-length");

            request = Unirest.post(urlBuilder.toString()).headers(headers);

            if (fileItems != null && !fileItems.isEmpty()) {
                for (FileItem fi : fileItems) {
                    if (fi.isFormField()) {
                        if (multipartBody == null) {
                            multipartBody = request.field(fi.getFieldName(), fi.getString(), fi.getContentType());
                        } else {
                            multipartBody = multipartBody.field(fi.getFieldName(), fi.getString(), fi.getContentType());
                        }
                    } else {
                        try {
                            if (multipartBody == null) {
                                multipartBody = request.field(fi.getFieldName(), fi.getInputStream(), ContentType.parse
                                        (fi.getContentType()), fi.getName());
                            } else {
                                multipartBody = multipartBody.field(fi.getFieldName(), fi.getInputStream(), ContentType
                                        .parse(fi.getContentType()), fi.getName());
                            }
                        } catch (Exception e) {
                            throw new ServiceException(HttpStatus.BAD_REQUEST,
                                    "Unable to process request.  \n" + e.getMessage());
                        }
                    }
                }
            }

            if (multipartBody != null) {
                multipartBody = multipartBody.mode(HttpMultipartMode.BROWSER_COMPATIBLE.toString());
                response = multipartBody.asString();
            }
        } catch (UnirestException ux) {
            throw new ServiceException(HttpStatus.valueOf(500), ux.getMessage());
        }

        handleErrorResponse(response);
        com.cloudelements.cesdk.util.HttpResponse httpResponse = new com.cloudelements.cesdk.util.HttpResponse();

        httpResponse.setHeaders(response.getHeaders());
        httpResponse.setRawBody(response.getRawBody());


        return httpResponse;

    }
}
