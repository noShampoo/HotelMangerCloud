package com.xust.hotel.common.exception;

/**
 * @author bhj
 */
public class NotDeleteException extends Exception {
    private static final long serialVersionUID = 7764238055258605504L;

    public NotDeleteException() {
        super();
    }

    public NotDeleteException(String message) {
        super(message);
    }

    public NotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotDeleteException(Throwable cause) {
        super(cause);
    }

    protected NotDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
