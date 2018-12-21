package com.cloudelements.cesdk.util;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse<T> {
    int code;
    Map<String, Object> headers;
    T body;
    Exception cause;
    InputStream rawBody;
    String rawStringBody;
    String statusText;

    public HttpResponse() {
        this.code = HttpStatus.SC_OK;
    }

    public HttpResponse(int code) {
        this.code = code;
    }

    public HttpResponse(T body) {
        this();
        this.body = body;
    }

    public HttpResponse(int code, Map<String, Object> headers, T body) {
        this(body);
        this.code = code;
        this.headers = headers;
    }

    public HttpResponse(int code, Map<String, Object> headers, T body, Exception cause) {
        this(code, headers, body);
        this.cause = cause;
    }

    public HttpResponse(int code, Map<String, Object> headers, T body, String statusText, Exception cause) {
        this(code);
        this.body = body;
        this.headers = headers;
        this.statusText = statusText;
        this.cause = cause;
    }

    public HttpResponse(int code, Map<String, Object> headers, T body, InputStream rawBody, String rawStringBody,
                        Exception cause, String statusText) {
        this(code, headers, body, cause);
        this.rawBody = rawBody;
        this.rawStringBody = rawStringBody;
        this.statusText = statusText;
    }

    public boolean isError() { return !(code >= 200 && code <= 207); }

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }

    public Map<String, Object> getHeaders() { return headers; }

    public void setHeaders(Map<String, Object> headers) { this.headers = headers; }

    public void addHeader(String name, Object value) {
        if (headers == null) { headers = new HashMap<>(); }
        headers.put(name, value);
    }

    public T getBody() { return body; }

    public void setBody(T body) { this.body = body; }

    public Exception getCause() { return cause; }

    public void setCause(Exception cause) { this.cause = cause; }

    public InputStream getRawBody() { return rawBody; }

    public void setRawBody(InputStream rawBody) { this.rawBody = rawBody; }

    public String getRawStringBody() { return rawStringBody; }

    public void setRawStringBody(String rawStringBody) { this.rawStringBody = rawStringBody; }

    public String getStatusText() { return statusText; }

    public void setStatusText(String statusText) { this.statusText = statusText; }

    /**
     * If the given object is a map, then we concat each JSON key and value into a string in order to provide the best
     * error message we can
     * @return The provider error message
     */
    public String getProviderError() {
        Object json = getBody();
        String rawBody = getRawStringBody();
        if (json == null && rawBody == null) { return null; }

        if (json instanceof Map) {
            StringBuilder errorBuilder = new StringBuilder();
            for (String jsonKey : ((Map<String, Object>) json).keySet()) {
                if (StringUtils.isNotBlank(errorBuilder.toString())) { errorBuilder.append(", "); }
                errorBuilder.append(jsonKey).append(" - ").append(((Map<String, Object>) json).get(jsonKey));
            }
            return errorBuilder.toString();
        }
        return rawBody;
    }

    @Override
    public String toString() {
        return String.format("code: %s, headers: %s, body: %s, cause: %s", code, headers, body, cause);
    }

    public Object getHeaderValue(String headerName) {
        Object value = getHeaders().get(headerName.toLowerCase());
        if (value != null) { return value; }
        value = getHeaders().get(headerName);
        if (value != null) { return value; }
        // Finally try all Upper case.
        return getHeaders().get(headerName.toUpperCase());
    }

    public String getHeaderStringValue(String headerName) {
        Object value = getHeaders().get(headerName.toLowerCase());
        if (value == null) { value = getHeaders().get(headerName); }
        // Finally try all Upper case.
        if (value == null) { getHeaders().get(headerName.toUpperCase()); }

        if (value instanceof String) {
            return (String) value;
        }

        return null;
    }

}
