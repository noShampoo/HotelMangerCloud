package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class ExpiredException extends Exception {
    private static final long serialVersionUID = 4779858152576817781L;

    public ExpiredException() {
        super();
    }

    public ExpiredException(String message) {
        super(message);
    }

    public ExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredException(Throwable cause) {
        super(cause);
    }

    protected ExpiredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
