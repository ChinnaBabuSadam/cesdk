package com.cloudelements.cesdk.element.freshdeskv2;

import com.cloudelements.cesdk.element.freshdeskv2.FreshdeskApiDeligate;
import com.cloudelements.cesdk.service.Service;
import com.cloudelements.cesdk.service.domain.PathParameters;
import com.cloudelements.cesdk.service.domain.Request;
import com.cloudelements.cesdk.service.domain.RequestMethod;
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
import java.nio.file.Path;
import java.util.HashMap;
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
    private static final String RESOURCE = "resource";
    private static final String METHOD = "method";
    private static final String PAYLOAD = "payload";
    private static final String PATH_PARAMETERS = "pathParameters";
    private static final String ELEMENT_NEXT_PAGE_TOKEN = "elements-next-page-token";
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";
    private static final String OBJECTS = "objects";

    private static final String AUTHORIZATION = "Authorization";
    private static final String header = "Basic cGNUbFFSWEFnMVlXZHhEd3N4SVg6WA==";

    FreshdeskApiDeligate freshdeskApiDeligate;


    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

        freshdeskApiDeligate = new FreshdeskApiDeligate();
        Map<String, String> headers = new HashMap<>();
        headers.put(AUTHORIZATION, header);
        headers.put(ServiceConstants.ACCEPT, APPLICATION_JSON);
        headers.put(ServiceConstants.CONTENT_TYPE, APPLICATION_JSON);
        freshdeskApiDeligate.setHeaders(headers);
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
        Map<String, Object> body = JacksonJsonUtil.convertStringToMap(bodyString);

        if (!validateRequestBody(body)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Insufficient data");
        }

        Request request = constructRequest(httpServletRequest, body);

        HttpResponse response = dispatchRequest(request);

        writeToOutputStream(httpServletResponse, response);
    }

    private void writeToOutputStream(HttpServletResponse httpServletResponse, HttpResponse response) {
        try {
            String responseString = JacksonJsonUtil.convertObjectToString(response.getBody());

            String nextPageToken = response.getHeaderStringValue(ELEMENT_NEXT_PAGE_TOKEN);
            if (StringUtils.isNotBlank(nextPageToken)) {
                httpServletResponse.addHeader(ELEMENT_NEXT_PAGE_TOKEN, nextPageToken);
            }
            httpServletResponse.addHeader(ServiceConstants.CONTENT_TYPE, APPLICATION_JSON);
            httpServletResponse.getOutputStream().write(responseString.getBytes());
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.NO_CONTENT, "Unable to retrieve response from provider");
        }
    }

    private HttpResponse dispatchRequest(Request request) {
        RequestMethod method = request.getMethod();
        HttpResponse httpResponse = new HttpResponse();
        Map<String, Object> headers = new HashMap<>();
        Object response = null;
        switch (method) {
            case CREATE:
                response = freshdeskApiDeligate.create(request.getResource(), request.getBody());
                httpResponse.setCode(201);
                break;
            case RETRIEVE:
                response = freshdeskApiDeligate.retrieve(request.getResource(), request.getPathParameters().getId());
                httpResponse.setCode(200);
                break;
            case UPDATE:
                response = freshdeskApiDeligate.update(request.getResource(), request.getPathParameters().getId(),
                        request.getBody());
                httpResponse.setCode(200);
                break;
            case DELETE:
                freshdeskApiDeligate.delete(request.getResource(), request.getPathParameters().getId());
                httpResponse.setCode(200);
                break;
            case SEARCH:
                if (StringUtils.equalsIgnoreCase(request.getResource(), OBJECTS)) {
                    response = freshdeskApiDeligate.findObjects();
                } else {
                    response = freshdeskApiDeligate.find(request.getResource(), request.getQueryParams());
                    loadNextPageTokenHeader(headers, request);
                }
                httpResponse.setCode(200);
                break;
            default:
                throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, "Method not supported");
        }

        httpResponse.setHeaders(headers);
        httpResponse.setBody(response);
        return httpResponse;
    }

    private void loadNextPageTokenHeader(Map<String, Object> headers, Request request) {
        Map<String, Object> queryParams = request.getQueryParams();
        JSONObject json = new JSONObject();
        int page = 0;
        int pageSize = 0;
        if (queryParams != null) {
            page = Integer.valueOf(String.valueOf(queryParams.get(PAGE)));
            pageSize = Integer.valueOf(String.valueOf(queryParams.get(PAGE_SIZE)));
        }
        json.put(PAGE, page + 1);
        json.put(PAGE_SIZE, pageSize);
        String nextPageToken = Base64.encodeBase64URLSafeString(json.toString().getBytes());
        headers.put(ELEMENT_NEXT_PAGE_TOKEN, nextPageToken);
    }

    private Request constructRequest(HttpServletRequest req, Map<String, Object> body) {

        try {
            String method = (String) body.get(METHOD);
            String resource = (String) body.get(RESOURCE);
            Map payload = null;
            Map headers = null;
            Map queryParams = null;
            PathParameters pathParameters = null;

            if (body.containsKey(PAYLOAD)) {
                payload = JacksonJsonUtil.convertObjectToMap(body.get(PAYLOAD));
            }

            if (body.containsKey(QUERY_PARAMETERS)) {
                Object params = body.get(QUERY_PARAMETERS);
                queryParams = JacksonJsonUtil.convertObjectToMap(params);
            }

            if (body.containsKey(PATH_PARAMETERS)) {
                String pathParams = JacksonJsonUtil.convertObjectToString(body.get(PATH_PARAMETERS));
                pathParameters = JacksonJsonUtil.convertStringToObject(pathParams, PathParameters.class);
            }

            return new Request(RequestMethod.valueOf(method), resource, payload, headers, queryParams, pathParameters);

        } catch (IllegalArgumentException ix) {
            throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, "The given mehtod not yet supported");
        } catch (JsonParseException jx) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "The payload or the queryparameters map is not well defined");
        } catch (Exception e) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "Unable to process the request.  Please make sure the request payload is bound to CE standards");
        }
    }


    private boolean validateRequestBody(Map<String, Object> body) {
        return body != null && body.containsKey(RESOURCE) && body.containsKey(METHOD);
    }
}
