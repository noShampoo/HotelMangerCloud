package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class NoSuchKeyException extends Exception {

    private static final long serialVersionUID = 6119277039193012284L;

    public NoSuchKeyException() {
        super();
    }

    public NoSuchKeyException(String message) {
        super(message);
    }

    public NoSuchKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchKeyException(Throwable cause) {
        super(cause);
    }

    protected NoSuchKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
