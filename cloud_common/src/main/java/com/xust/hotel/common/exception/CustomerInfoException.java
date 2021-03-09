package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class CustomerInfoException extends Exception {
    private static final long serialVersionUID = -5724937685862923210L;

    public CustomerInfoException() {
        super();
    }

    public CustomerInfoException(String message) {
        super(message);
    }

    public CustomerInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerInfoException(Throwable cause) {
        super(cause);
    }

    protected CustomerInfoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
