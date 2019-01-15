package com.cloudelements.cesdk.service.domain;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.http.HttpMethod;

import java.io.Serializable;
import java.util.List;
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

    PagingDetails brokerPagingDetails;

    BrokerConfiguration brokerConfiguration;

    List<FileItem> multipart;

    Map<String, Object> multipartFormBody;

    Object provisionConfigs;

    List<Query> whereExpression;

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

    public Request(List<FileItem> fileItems, HttpMethod method, ResourceOperation resourceOperation, Map<String,
            Object> headers) {
        this.multipart = fileItems;
        this.method = method;
        this.resourceOperation = resourceOperation;
        this.headers =  headers;
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
            Object> headers, Map<String, Object> queryParameters, PathParameters pathParameters,
                   PagingDetails brokerPagingDetails, BrokerConfiguration brokerConfiguration,
                   List<FileItem> multipart, Map<String, Object> multipartFormBody, Object provisionConfigs,
                   List<Query> whereExpression) {
        this.resourceOperation = resourceOperation;
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParameters = queryParameters;
        this.pathParameters = pathParameters;
        this.brokerPagingDetails = brokerPagingDetails;
        this.brokerConfiguration = brokerConfiguration;
        this.multipart = multipart;
        this.multipartFormBody = multipartFormBody;
        this.provisionConfigs = provisionConfigs;
        this.whereExpression = whereExpression;
    }

    public Request(ResourceOperation resourceOperation, HttpMethod method, String resource, Map body, Map<String,
            Object> headers, Map<String, Object> queryParams, PathParameters pathParameters,
                   PagingDetails brokerPagingDetails, BrokerConfiguration brok) {
        this.resourceOperation = resourceOperation;
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParameters = queryParams;
        this.pathParameters = pathParameters;
        this.brokerPagingDetails = brokerPagingDetails;
        this.brokerConfiguration = brok;
    }

    public Request(ResourceOperation resourceOperation, HttpMethod method, String resource, Map body, Map<String,
            Object> headers, Map<String, Object> queryParams, PathParameters pathParameters,
                   PagingDetails brokerPagingDetails, BrokerConfiguration brok, Object provisionConfigs) {
        this.resourceOperation = resourceOperation;
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParameters = queryParams;
        this.pathParameters = pathParameters;
        this.brokerPagingDetails = brokerPagingDetails;
        this.brokerConfiguration = brok;
        this.provisionConfigs = provisionConfigs;
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

    public PagingDetails getBrokerPagingDetails() {
        return brokerPagingDetails;
    }

    public void setBrokerPagingDetails(PagingDetails brokerPagingDetails) {
        this.brokerPagingDetails = brokerPagingDetails;
    }

    public BrokerConfiguration getBrokerConfiguration() {
        return brokerConfiguration;
    }

    public void setBrokerConfiguration(BrokerConfiguration brokerConfiguration) {
        this.brokerConfiguration = brokerConfiguration;
    }

    public List<FileItem> getMultipart() {
        return multipart;
    }

    public void setMultipart(List<FileItem> multipart) {
        this.multipart = multipart;
    }

    public Map<String, Object> getMultipartFormBody() {
        return multipartFormBody;
    }

    public void setMultipartFormBody(Map<String, Object> multipartFormBody) {
        this.multipartFormBody = multipartFormBody;
    }

    public boolean isMultipartRequest() {
        return this.multipart != null && !multipart.isEmpty();
    }

    public Object getProvisionConfigs() {
        return provisionConfigs;
    }

    public void setProvisionConfigs(Object provisionConfigs) {
        this.provisionConfigs = provisionConfigs;
    }

    public List<Query> getWhereExpression() {
        return whereExpression;
    }

    public void setWhereExpression(List<Query> whereExpression) {
        this.whereExpression = whereExpression;
    }
}
