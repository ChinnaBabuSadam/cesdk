package com.cloudelements.cesdk.service.exception;

/**
 * Created by brad on 2/12/15.
 */
public class InvalidFieldException extends RuntimeException {
    public InvalidFieldException() { super(); }

    public InvalidFieldException(String message, Exception cause) { super(message, cause);}

    public InvalidFieldException(Exception cause) { super(cause);}
}