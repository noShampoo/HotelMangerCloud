package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class CanNotReserveException extends Exception {
    private static final long serialVersionUID = -6519064443858476415L;

    public CanNotReserveException() {
        super();
    }

    public CanNotReserveException(String message) {
        super(message);
    }

    public CanNotReserveException(String message, Throwable cause) {
        super(message, cause);
    }

    public CanNotReserveException(Throwable cause) {
        super(cause);
    }

    protected CanNotReserveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
