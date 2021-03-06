package com.cloudelements.cesdk.element.freshdeskv2;

import com.cloudelements.cesdk.service.domain.BrokerConfig;
import com.cloudelements.cesdk.service.domain.BrokerRequest;
import com.cloudelements.cesdk.service.domain.BrokerResourceOperation;
import com.cloudelements.cesdk.service.exception.JsonParseException;
import com.cloudelements.cesdk.service.exception.ServiceException;
import com.cloudelements.cesdk.util.HttpResponse;
import com.cloudelements.cesdk.util.JacksonJsonUtil;
import com.cloudelements.cesdk.util.ServiceConstants;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "FreshDeskService",
        description = "This servlet helps to invoke all endpoints of FreshDesk v2 service",
        urlPatterns = {"/freshdeskv2"}
)
public class FreshDeskService extends HttpServlet {

    private static final String APPLICATION_JSON = "application/json";
    private static final String MESSAGE = "message";
    private static final String QUERY_PARAMETERS = "queryParameters";
    private static final String BROKER_RESOURCE = "brokerResource";
    private static final String BROKER_METHOD = "brokerMethod";
    private static final String BROKER_RESOURCE_OPERATION = "brokerResourceOperation";
    private static final String PAYLOAD = "payload";
    private static final String PATH_PARAMETERS = "pathParameters";
    private static final String ELEMENT_NEXT_PAGE_TOKEN = "elements-next-page-token";
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";
    private static final String OBJECTS = "objects";
    private static final String BODY = "body";

    private static final String AUTHORIZATION = "Authorization";

    FreshdeskApiDeligate freshdeskApiDeligate;


    @Override
    public void init() throws ServletException {
        super.init();
    }

    private void initDeligate(BrokerRequest request) {
        freshdeskApiDeligate = new FreshdeskApiDeligate();
        Map<String, String> headers = new HashMap<>();
        List<BrokerConfig> authConfigs = request.getBrokerAuthenticationConfigs();
        for (BrokerConfig config: authConfigs) {
            headers.put(config.getKey(), config.getValue());
        }

        headers.put(ServiceConstants.ACCEPT, APPLICATION_JSON);
        headers.put(ServiceConstants.CONTENT_TYPE, APPLICATION_JSON);
        freshdeskApiDeligate.setHeaders(headers);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        try {
            processRequest(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
            JSONObject jsonObject = new JSONObject();
            if (e instanceof ServiceException) {
                HttpStatus httpStatus = ((ServiceException) e).getHttpStatus();
                String errorMessage = ((ServiceException) e).getErrorMessage();
                jsonObject.put(MESSAGE, errorMessage);

                httpServletResponse.setStatus(httpStatus.value());
                httpServletResponse.getOutputStream().write(jsonObject.toString().getBytes());
            } else {
                jsonObject.put(MESSAGE, "Internal Server Error");
                httpServletResponse.setStatus(500);
                httpServletResponse.getOutputStream().write(jsonObject.toString().getBytes());
            }
        }
    }


    private void processRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String bodyString;

        InputStream inputStream = httpServletRequest.getInputStream();
        bodyString = IOUtils.toString(inputStream, ServiceConstants.UTF_8);
        Map<String, Object> payload = JacksonJsonUtil.convertStringToMap(bodyString);

        if (!validateRequestBody(payload)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Insufficient data");
        }

        BrokerRequest request = constructRequest(httpServletRequest, payload);
        initDeligate(request);

        HttpResponse response = dispatchRequest(request);

        writeToOutputStream(httpServletResponse, response);
    }

    private void writeToOutputStream(HttpServletResponse httpServletResponse, HttpResponse response) {
        try {
            Object responseBody = response.getBody();
            String responseString = null;
            if (responseBody != null) {
                responseString = JacksonJsonUtil.convertObjectToString(response.getBody());
            }

            String nextPageToken = response.getHeaderStringValue(ELEMENT_NEXT_PAGE_TOKEN);
            if (StringUtils.isNotBlank(nextPageToken)) {
                httpServletResponse.addHeader(ELEMENT_NEXT_PAGE_TOKEN, nextPageToken);
            }

            Map<String, Object> headers = response.getHeaders();
            for (String key : headers.keySet()) {
                Object headerVal = headers.get(key);
                if (headerVal instanceof List) {
                    httpServletResponse.addHeader(key, String.valueOf(((List) headerVal).get(0)));
                } else {
                    httpServletResponse.addHeader(key, String.valueOf(headers.get(key)));
                }
            }

            if (StringUtils.isBlank(responseString)) {
                IOUtils.copyLarge(response.getRawBody(), httpServletResponse.getOutputStream());
            } else {
                httpServletResponse.getOutputStream().write(responseString.getBytes());
            }
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.NO_CONTENT, "Unable to retrieve response from provider");
        }
    }

    private HttpResponse dispatchRequest(BrokerRequest request) {
        BrokerResourceOperation method = request.getBrokerResourceOperation();
        HttpResponse httpResponse = new HttpResponse();
        Map<String, Object> headers = new HashMap<>();
        Object response = null;
        switch (method) {
            case INIT:
                response = freshdeskApiDeligate.fetchSchema();
                httpResponse.setCode(200);
                break;
            case REFRESH:
                response = freshdeskApiDeligate.refresh();
                httpResponse.setCode(200);
                break;
            case CREATE:
                response = freshdeskApiDeligate.create(request.getBrokerResource(), request.getBrokerBody());
                httpResponse.setCode(201);
                break;
            case RETRIEVE:
                response = freshdeskApiDeligate.retrieve(request.getBrokerResource(), request.getBrokerPathParameters().getId());
                httpResponse.setCode(200);
                break;
            case UPDATE:
                response = freshdeskApiDeligate.update(request.getBrokerResource(), request.getBrokerPathParameters().getId(),
                        request.getBrokerBody());
                httpResponse.setCode(200);
                break;
            case DELETE:
                freshdeskApiDeligate.delete(request.getBrokerResource(), request.getBrokerPathParameters().getId());
                httpResponse.setCode(200);
                break;
            case SEARCH:
                if (StringUtils.equalsIgnoreCase(request.getBrokerResource(), OBJECTS)) {
                    response = freshdeskApiDeligate.findObjects();
                } else {
                    response = freshdeskApiDeligate.find(request.getBrokerResource(), request.getBrokerExpressions());
                    loadNextPageTokenHeader(headers, request);
                }
                httpResponse.setCode(200);
                break;
            default:
                throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, "Method not supported");
        }

        httpResponse.setHeaders(headers);
        httpResponse.setHeaders(headers);
        if (response instanceof InputStream) {
            httpResponse.setRawBody((InputStream) response);
        } else {
            httpResponse.setBody(response);
        }
        return httpResponse;
    }

    private void loadNextPageTokenHeader(Map<String, Object> headers, BrokerRequest request) {
        Map<String, Object> queryParams = request.getBrokerQueryParameters();
        JSONObject json = new JSONObject();
        int page = 0;
        int pageSize = 200;
        if (queryParams != null) {
            if (queryParams.containsKey(PAGE)) {
                page = Integer.valueOf(String.valueOf(queryParams.get(PAGE)));
            }
            if (queryParams.containsKey(PAGE_SIZE)) {
                pageSize = Integer.valueOf(String.valueOf(queryParams.get(PAGE_SIZE)));
            }
        }
        json.put(PAGE, page + 1);
        json.put(PAGE_SIZE, pageSize);
        String nextPageToken = Base64.encodeBase64URLSafeString(json.toString().getBytes());
        headers.put(ELEMENT_NEXT_PAGE_TOKEN, nextPageToken);
    }

    private BrokerRequest constructRequest(HttpServletRequest req, Map<String, Object> payload) {

        try {

            return JacksonJsonUtil.convertStringToObject(JacksonJsonUtil.convertMapToString(payload),
                    BrokerRequest.class);
        } catch (IllegalArgumentException ix) {
            throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, "The given method not yet supported");
        } catch (JsonParseException jx) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "The request payload is not well defined");
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "Unable to process the request.  Please make sure the request payload is bound to CE standards");
        }
    }


    private boolean validateRequestBody(Map<String, Object> body) {
        return body != null &&
                body.containsKey(BROKER_RESOURCE) &&
                body.containsKey(BROKER_METHOD) &&
                body.containsKey(BROKER_RESOURCE_OPERATION);
    }
}
