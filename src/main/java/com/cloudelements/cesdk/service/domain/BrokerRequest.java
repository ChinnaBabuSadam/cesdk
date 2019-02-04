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

    BrokerResourceOperation brokerResourceOperation;

    HttpMethod brokerMethod;

    String brokerResource;

    Map brokerBody;

    Map<String, Object> brokerHeaders;

    Map<String, Object> brokerQueryParameters;

    BrokerPathParameters brokerPathParameters;

    BrokerPagingDetails brokerPagingDetails;

    List<FileItem> brokerMultipart;

    Map<String, Object> brokerMultipartFormBody;

    List<BrokerConfig> brokerAuthenticationConfigs;

    List<BrokerExpression> brokerExpressions;


    public BrokerRequest() {}

    public BrokerRequest(BrokerResourceOperation brokerResourceOperation, HttpMethod brokerMethod, String brokerResource, Map brokerBody,
                         Map<String, Object> brokerHeaders, Map<String, Object> brokerQueryParameters,
                         BrokerPathParameters brokerPathParameters, BrokerPagingDetails brokerPagingDetails,
                         List<FileItem> brokerMultipart, Map<String, Object> brokerMultipartFormBody,
                         List<BrokerConfig> brokerAuthenticationConfigs, List<BrokerExpression> brokerExpressions) {
        this.brokerResourceOperation = brokerResourceOperation;
        this.brokerMethod = brokerMethod;
        this.brokerResource = brokerResource;
        this.brokerBody = brokerBody;
        this.brokerHeaders = brokerHeaders;
        this.brokerQueryParameters = brokerQueryParameters;
        this.brokerPathParameters = brokerPathParameters;
        this.brokerPagingDetails = brokerPagingDetails;
        this.brokerMultipart = brokerMultipart;
        this.brokerMultipartFormBody = brokerMultipartFormBody;
        this.brokerAuthenticationConfigs = brokerAuthenticationConfigs;
        this.brokerExpressions = brokerExpressions;
    }

    public HttpMethod getBrokerMethod() {
        return brokerMethod;
    }

    public void setBrokerMethod(HttpMethod brokerMethod) {
        this.brokerMethod = brokerMethod;
    }

    public BrokerResourceOperation getBrokerResourceOperation() {
        return brokerResourceOperation;
    }

    public void setBrokerResourceOperation(BrokerResourceOperation brokerResourceOperation) {
        this.brokerResourceOperation = brokerResourceOperation;
    }

    public String getBrokerResource() {
        return brokerResource;
    }

    public void setBrokerResource(String brokerResource) {
        this.brokerResource = brokerResource;
    }

    public Map getBrokerBody() {
        return brokerBody;
    }

    public void setBrokerBody(Map brokerBody) {
        this.brokerBody = brokerBody;
    }

    public Map<String, Object> getBrokerHeaders() {
        return brokerHeaders;
    }

    public void setBrokerHeaders(Map<String, Object> brokerHeaders) {
        this.brokerHeaders = brokerHeaders;
    }

    public Map<String, Object> getBrokerQueryParameters() {
        return brokerQueryParameters;
    }

    public void setBrokerQueryParameters(Map<String, Object> brokerQueryParameters) {
        this.brokerQueryParameters = brokerQueryParameters;
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

    public List<FileItem> getBrokerMultipart() {
        return brokerMultipart;
    }

    public void setBrokerMultipart(List<FileItem> brokerMultipart) {
        this.brokerMultipart = brokerMultipart;
    }

    public Map<String, Object> getBrokerMultipartFormBody() {
        return brokerMultipartFormBody;
    }

    public void setBrokerMultipartFormBody(Map<String, Object> brokerMultipartFormBody) {
        this.brokerMultipartFormBody = brokerMultipartFormBody;
    }

    public List<BrokerConfig> getBrokerAuthenticationConfigs() {
        return brokerAuthenticationConfigs;
    }

    public void setBrokerAuthenticationConfigs(List<BrokerConfig> brokerAuthenticationConfigs) {
        this.brokerAuthenticationConfigs = brokerAuthenticationConfigs;
    }

    public List<BrokerExpression> getBrokerExpressions() {
        return brokerExpressions;
    }

    public void setBrokerExpressions(List<BrokerExpression> brokerExpressions) {
        this.brokerExpressions = brokerExpressions;
    }

    public boolean isMultipartRequest() {
        return this.brokerMultipart != null && !brokerMultipart.isEmpty();
    }
}
