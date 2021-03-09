package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class BizInfoErrorException extends Exception {
    private static final long serialVersionUID = -583770396448307115L;

    public BizInfoErrorException() {
        super();
    }

    public BizInfoErrorException(String message) {
        super(message);
    }

    public BizInfoErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizInfoErrorException(Throwable cause) {
        super(cause);
    }

    protected BizInfoErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
