package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class KeyExistException extends Exception {
    private static final long serialVersionUID = 5345460127408316504L;

    public KeyExistException() {
        super();
    }

    public KeyExistException(String message) {
        super(message);
    }

    public KeyExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public KeyExistException(Throwable cause) {
        super(cause);
    }

    protected KeyExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
