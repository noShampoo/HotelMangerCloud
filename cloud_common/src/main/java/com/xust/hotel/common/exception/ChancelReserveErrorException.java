package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class ChancelReserveErrorException extends Exception {
    private static final long serialVersionUID = -7441541799572901575L;

    public ChancelReserveErrorException() {
        super();
    }

    public ChancelReserveErrorException(String message) {
        super(message);
    }

    public ChancelReserveErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChancelReserveErrorException(Throwable cause) {
        super(cause);
    }

    protected ChancelReserveErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
