/**
  * JsonConversionException.java $Revision: 1 $
  *
  * Copyright (c) 2010-2011 2Qrius Ltd. All rights reserved.
  * 2QRIUS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
  */

package com.cloudelements.cesdk.service.exception;

/**
 * An exception thrown when errors occur during JSON conversion.
 * 
 * @author Vineet Joshi
 * 
 * @version $Revision: 1 $
 */
public class JsonConversionException extends RuntimeException
{
    /**
     * The generated unique serial ID for the class.
     */
    private static final long serialVersionUID = -6153571465802696208L;

    /**
     * The default constructor.
     */
    public JsonConversionException()
    {
        super();
    }

    /**
     * A constructor with the error message and the cause of the error.
     * 
     * @param message The error message
     * @param cause The cause of the error.
     */
    public JsonConversionException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * A constructor with the error message.
     * 
     * @param message The error message
     */
    public JsonConversionException(String message)
    {
        super(message);
    }
    
    /**
     * A constructor with the cause of the error.
     * 
     * @param cause The cause of the error.
     */
    public JsonConversionException(Throwable cause)
    {
        super(cause);
    }
}
