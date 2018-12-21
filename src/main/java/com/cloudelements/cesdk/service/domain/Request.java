package com.cloudelements.cesdk.service.domain;

import java.io.Serializable;
import java.util.Map;

public class Request implements Serializable {

    private static final long serialVersionUID = -3705268137686288556L;

    public Request(){

    }

    public Request(RequestMethod method, String resource, Map body, Map<String, Object> headers,
                   Map<String, Object> queryParams, PathParameters pathParameters) {
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.headers = headers;
        this.queryParams = queryParams;
        this.pathParameters = pathParameters;
    }

    public Request(RequestMethod method, String resource) {
        this.method = method;
        this.resource = resource;
    }

    public Request(RequestMethod method, String resource, Map body) {
        this.method = method;
        this.resource = resource;
        this.body = body;
    }

    public Request(RequestMethod method, String resource, Map body, Map<String, Object> queryParams) {
        this.method = method;
        this.resource = resource;
        this.body = body;
        this.queryParams = queryParams;
    }



    RequestMethod method;

    String resource;

    Map body;

    Map<String, Object> headers;

    Map<String, Object> queryParams;

    PathParameters pathParameters;

    public RequestMethod getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method;
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

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    public PathParameters getPathParameters() {
        return pathParameters;
    }

    public void setPathParameters(PathParameters pathParameters) {
        this.pathParameters = pathParameters;
    }
}
