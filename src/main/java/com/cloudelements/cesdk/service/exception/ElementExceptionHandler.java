package com.cloudelements.cesdk.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ElementExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Object> handleServiceException(ServiceException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getHttpStatus(), ex.getErrorMessage());
        return buildResponseEntity(errorMessage);
    }

    private ResponseEntity<Object> buildResponseEntity(ErrorMessage apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Object> handleException(Exception ex) {
        ErrorMessage errorMessage =
                new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR);
        return buildResponseEntity(errorMessage);
    }

}
