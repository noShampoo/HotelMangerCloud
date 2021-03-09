package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class InnerErrorException extends Exception {
    private static final long serialVersionUID = -5703617287266508794L;

    public InnerErrorException() {
        super();
    }

    public InnerErrorException(String message) {
        super(message);
    }

    public InnerErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InnerErrorException(Throwable cause) {
        super(cause);
    }

    protected InnerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
