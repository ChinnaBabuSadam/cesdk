package com.cloudelements.cesdk.service.exception;

/**
 * This exception indicates that while we were trying to serialize a JSON object into a POJO,
 * there  was some field that was not recognizable and therefore the serialization failed.
 * @author jjwyse
 */
public class UnknownFieldException extends RuntimeException {
    public UnknownFieldException() { super(); }

    public UnknownFieldException(String message, Exception cause) { super(message, cause);}

    public UnknownFieldException(Exception cause) { super(cause);}
}
