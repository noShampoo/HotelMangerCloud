package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class NotChangeException extends Exception {
    private static final long serialVersionUID = 7764238055258605504L;

    public NotChangeException() {
        super();
    }

    public NotChangeException(String message) {
        super(message);
    }

    public NotChangeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotChangeException(Throwable cause) {
        super(cause);
    }

    protected NotChangeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
