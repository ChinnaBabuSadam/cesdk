package com.cloudelements.cesdk.service.domain;

import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Chinna This class represents Cloud Elements standard way of to make a request over HTTP. This is actually,
 * replicating RESTful in CE way... to support SDK broker operations.
 *
 * Notice that, everything goes as part of 'requestBody' over a POST API call, aimed at the service's base url
 */
public class Request implements Serializable {

    private static final long serialVersionUID = -3705268137686288556L;

    ResourceOperation resourceOperation;

    HttpMethod method;

    String resource;

    Map body;

    Map<String, Object> headers;

    Map<String, Object> queryParameters;

    PathParameters pathParameters;

    PagingDetails pagingDetails;

    BrokerConfiguration brokerConfiguration;

    public Request() {}

    public Request(HttpMethod method, ResourceOperation resourceOperation, String resource, Map body,
                   Map<String, Object> headers,
                   Map<String, Object> queryParams, PathParameters pathParameters) {
        this.resourceOperation = resourceOperation;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParameters = queryParams;
        this.pathParameters = pathParameters;
    }

    public Request(ResourceOperation resourceOperation, String resource) {
        this.resourceOperation = resourceOperation;
        this.resource = resource;
    }

    public Request(ResourceOperation resourceOperation, String resource, Map body) {
        this.resourceOperation = resourceOperation;
        this.resource = resource;
        this.body = body;
    }

    public Request(ResourceOperation resourceOperation, String resource, Map body, Map<String, Object> queryParams) {
        this.resourceOperation = resourceOperation;
        this.resource = resource;
        this.body = body;
        this.queryParameters = queryParams;
    }

    public Request(ResourceOperation resourceOperation, HttpMethod method, String resource, Map body, Map<String,
            Object> headers, Map<String, Object> queryParams, PathParameters pathParameters,
                   PagingDetails pagingDetails, BrokerConfiguration brok) {
        this.resourceOperation = resourceOperation;
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParameters = queryParams;
        this.pathParameters = pathParameters;
        this.pagingDetails = pagingDetails;
        this.brokerConfiguration = brok;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public ResourceOperation getResourceOperation() {
        return resourceOperation;
    }

    public void setResourceOperation(ResourceOperation resourceOperation) {
        this.resourceOperation = resourceOperation;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Map getBody() {
        return body;
    }

    public void setBody(Map body) {
        this.body = body;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(Map<String, Object> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public PathParameters getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(PathParameters pathParameters) {
        this.pathParameters = pathParameters;
    }

    public PagingDetails getPagingDetails() {
        return pagingDetails;
    }

    public void setPagingDetails(PagingDetails pagingDetails) {
        this.pagingDetails = pagingDetails;
    }

    public BrokerConfiguration getBrokerConfiguration() {
        return brokerConfiguration;
    }

    public void setBrokerConfiguration(BrokerConfiguration brokerConfiguration) {
        this.brokerConfiguration = brokerConfiguration;
    }

}
