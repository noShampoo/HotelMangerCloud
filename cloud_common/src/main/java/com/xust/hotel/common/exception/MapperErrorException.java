package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class MapperErrorException extends Exception {
    private static final long serialVersionUID = 4684615758245289084L;

    public MapperErrorException() {
        super();
    }

    public MapperErrorException(String message) {
        super(message);
    }

    public MapperErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapperErrorException(Throwable cause) {
        super(cause);
    }

    protected MapperErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
