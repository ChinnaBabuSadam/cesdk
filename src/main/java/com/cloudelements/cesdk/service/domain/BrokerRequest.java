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
public class BrokerRequest implements Serializable {

    private static final long serialVersionUID = -3705268137686288556L;

    ResourceOperation resourceOperation;

    HttpMethod method;

    String resource;

    Map body;

    Map<String, Object> headers;

    Map<String, Object> queryParameters;

    BrokerPathParameters brokerPathParameters;

    BrokerPagingDetails brokerPagingDetails;

    List<FileItem> multipart;

    Map<String, Object> multipartFormBody;

    List<BrokerConfig> authenticationConfigs;

    List<BrokerExpression> brokerExpressions;


    public BrokerRequest() {}

    public BrokerRequest(ResourceOperation resourceOperation, HttpMethod method, String resource, Map body,
                         Map<String, Object> headers, Map<String, Object> queryParameters,
                         BrokerPathParameters brokerPathParameters, BrokerPagingDetails brokerPagingDetails,
                         List<FileItem> multipart, Map<String, Object> multipartFormBody,
                         List<BrokerConfig> authenticationConfigs, List<BrokerExpression> brokerExpressions) {
        this.resourceOperation = resourceOperation;
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParameters = queryParameters;
        this.brokerPathParameters = brokerPathParameters;
        this.brokerPagingDetails = brokerPagingDetails;
        this.multipart = multipart;
        this.multipartFormBody = multipartFormBody;
        this.authenticationConfigs = authenticationConfigs;
        this.brokerExpressions = brokerExpressions;
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

    public BrokerPathParameters getBrokerPathParameters() {
        return brokerPathParameters;
    }

    public void setBrokerPathParameters(BrokerPathParameters brokerPathParameters) {
        this.brokerPathParameters = brokerPathParameters;
    }

    public BrokerPagingDetails getBrokerPagingDetails() {
        return brokerPagingDetails;
    }

    public void setBrokerPagingDetails(BrokerPagingDetails brokerPagingDetails) {
        this.brokerPagingDetails = brokerPagingDetails;
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

    public List<BrokerConfig> getAuthenticationConfigs() {
        return authenticationConfigs;
    }

    public void setAuthenticationConfigs(List<BrokerConfig> authenticationConfigs) {
        this.authenticationConfigs = authenticationConfigs;
    }

    public List<BrokerExpression> getBrokerExpressions() {
        return brokerExpressions;
    }

    public void setBrokerExpressions(List<BrokerExpression> brokerExpressions) {
        this.brokerExpressions = brokerExpressions;
    }

    public boolean isMultipartRequest() {
        return this.multipart != null && !multipart.isEmpty();
    }
}
