package com.cloudelements.cesdk.service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

/** The class to construct an error message for exception handling. */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private HttpStatus status;
    private String errorMessgae;
    private Throwable cause;

    public ErrorMessage(HttpStatus status) {
        this.status = status;
    }

    public ErrorMessage(HttpStatus status, String errorMessgae) {
        this.status = status;

        this.errorMessgae = errorMessgae;
    }

    public ErrorMessage(HttpStatus status, String errorMessgae, Throwable ex) {
        this.errorMessgae = errorMessgae;
        this.status = status;
    }

    public Throwable getCause() {
        return cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public String getErrorMessgae() {
        return errorMessgae;
    }

    public void setErrorMessgae(String errorMessgae) {
        this.errorMessgae = errorMessgae;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
