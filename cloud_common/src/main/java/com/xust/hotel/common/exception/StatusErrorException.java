package com.xust.hotel.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author bhj
 */
public class StatusErrorException extends Exception {
    private static final long serialVersionUID = 5911899907650817342L;

    public StatusErrorException() {
        super();
    }

    public StatusErrorException(String message) {
        super(message);
    }

    public StatusErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusErrorException(Throwable cause) {
        super(cause);
    }

    protected StatusErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
