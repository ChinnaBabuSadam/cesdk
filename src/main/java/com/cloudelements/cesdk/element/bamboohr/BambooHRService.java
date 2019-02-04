package com.cloudelements.cesdk.element.bamboohr;

import com.cloudelements.cesdk.element.freshdeskv2.FreshdeskApiDeligate;
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
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(
        name = "BambooHRService",
        description = "This servlet helps to invoke all endpoints of FreshDesk v2 service",
        urlPatterns = {"/bamboohr"}
)
public class BambooHRService extends HttpServlet {
    private static final String APPLICATION_JSON = "application/json";
    private static final String MESSAGE = "message";
    private static final String ELEMENT_NEXT_PAGE_TOKEN = "elements-next-page-token";
    private static final String PAGE = "page";
    private static final String PAGE_SIZE = "pageSize";
    private static final String OBJECTS = "objects";
    private static final String BROKER_RESOURCE = "brokerResource";
    private static final String BROKER_METHOD = "brokerMethod";
    private static final String BROKER_RESOURCE_OPERATION = "brokerResourceOperation";
    private static final String AUTHORIZATION = "Authorization";

    BambooHRApiDeligate bamboohrApiDeligate;


    @Override
    public void init() throws ServletException {
        super.init();
    }

    private void initDeligate(BrokerRequest request) {
        bamboohrApiDeligate = new BambooHRApiDeligate();
        Map<String, String> headers = new HashMap<>();
        List<BrokerConfig> authConfigs = request.getBrokerAuthenticationConfigs();
        for (BrokerConfig config: authConfigs) {
            headers.put(config.getKey(), config.getValue());
        }

        headers.put(ServiceConstants.ACCEPT, APPLICATION_JSON);
        headers.put(ServiceConstants.CONTENT_TYPE, APPLICATION_JSON);
        bamboohrApiDeligate.setHeaders(headers);
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

        BrokerRequest request = null;

        if (ServletFileUpload.isMultipartContent(httpServletRequest)) {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();

            // Configure a repository (to ensure a secure temp location is used)
            ServletContext servletContext = this.getServletConfig().getServletContext();

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);

            // Parse the request
            List<FileItem> items = upload.parseRequest(new ServletRequestContext(httpServletRequest));
            request = constructMultipartRequest(httpServletRequest, items);
        } else {
            InputStream inputStream = httpServletRequest.getInputStream();
            String bodyString = IOUtils.toString(inputStream, ServiceConstants.UTF_8);
            Map<String, Object> payload = JacksonJsonUtil.convertStringToMap(bodyString);
            if (!validateRequestBody(payload)) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "Insufficient data");
            }
            request = constructRequest(httpServletRequest, payload);
        }

        initDeligate(request);

        HttpResponse response = dispatchRequest(request);

        writeToOutputStream(httpServletResponse, response);
    }

    private BrokerRequest constructMultipartRequest(HttpServletRequest httpServletRequest, List<FileItem> items) {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Map<String, Object> headers = new HashMap<>();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String headerKey = headerNames.nextElement();
                headers.put(headerKey, httpServletRequest.getHeader(headerKey));
            }
        }
        /**
         * public BrokerRequest(ResourceOperation resourceOperation, HttpMethod method, String resource, Map body,
         *                          Map<String, Object> headers, Map<String, Object> queryParameters,
         *                          BrokerPathParameters brokerPathParameters, BrokerPagingDetails brokerPagingDetails,
         *                          List<FileItem> multipart, Map<String, Object> multipartFormBody,
         *                          List<BrokerConfig> authenticationConfigs, List<BrokerExpression> brokerExpressions)
         */

        return new BrokerRequest(BrokerResourceOperation.CREATE, HttpMethod.valueOf(httpServletRequest.getMethod()), null, null,
                headers, null, null, null, items, null, null, null);
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
                response = bamboohrApiDeligate.fetchSchema();
                httpResponse.setCode(200);
                break;
            case CREATE:
                if (request.isMultipartRequest()) {
                    response = bamboohrApiDeligate.postFile("files", request.getBrokerMultipart(),
                            request.getBrokerMultipartFormBody(), request.getBrokerHeaders());
                } else {
                    response = bamboohrApiDeligate.create(request.getBrokerResource(), request.getBrokerBody());
                }
                httpResponse.setCode(201);
                break;
            case RETRIEVE:
                if (StringUtils.equalsIgnoreCase(request.getBrokerResource(), "files")) {
                    response = bamboohrApiDeligate.retrieveFile(request.getBrokerResource(),
                            request.getBrokerPathParameters().getId());
                } else {
                    response = bamboohrApiDeligate.retrieve(request.getBrokerResource(), request.getBrokerPathParameters().getId());
                }
                httpResponse.setCode(200);
                break;
            case UPDATE:
                response = bamboohrApiDeligate.update(request.getBrokerResource(), request.getBrokerPathParameters().getId(),
                        request.getBrokerBody());
                httpResponse.setCode(200);
                break;
            case DELETE:
                bamboohrApiDeligate.delete(request.getBrokerResource(), request.getBrokerPathParameters().getId());
                httpResponse.setCode(200);
                break;
            case SEARCH:
                if (StringUtils.equalsIgnoreCase(request.getBrokerResource(), OBJECTS)) {
                    response = bamboohrApiDeligate.findObjects();
                } else {
                    response = bamboohrApiDeligate.find(request.getBrokerResource(), request.getBrokerQueryParameters());
                    loadNextPageTokenHeader(headers, request);
                }
                httpResponse.setCode(200);
                break;
            default:
                throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, "Method not supported");
        }

        httpResponse.setHeaders(headers);
        if (response instanceof HttpResponse) {

            return (HttpResponse) response;
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
            return JacksonJsonUtil.convertStringToObject(JacksonJsonUtil.convertMapToString(payload), BrokerRequest.class);
        } catch (IllegalArgumentException ix) {
            throw new ServiceException(HttpStatus.METHOD_NOT_ALLOWED, "The given mehtod not yet supported");
        } catch (JsonParseException jx) {
            throw new ServiceException(HttpStatus.BAD_REQUEST,
                    "The payload or the queryParameters map is not well defined");
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
