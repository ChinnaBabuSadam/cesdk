package com.cloudelements.cesdk.service.exception;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -5080964464878895329L;


    private HttpStatus httpStatus;
    private String errorMessage;
    private String errorDetails;
    private String providerErrorMessage;


    public ServiceException(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }


    public ServiceException(HttpStatus httpStatus, String errorMessage, String errorDetails) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
    }


    public ServiceException(HttpStatus httpStatus, String errorMessage, String errorDetails,
                            String providerErrorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
        this.providerErrorMessage = providerErrorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public String getProviderErrorMessage() {
        return providerErrorMessage;
    }

    public void setProviderErrorMessage(String providerErrorMessage) {
        this.providerErrorMessage = providerErrorMessage;
    }
}
