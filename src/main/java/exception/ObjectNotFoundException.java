/*
 * ObjectNotFoundException.java
 * Copyright 2019 Qunhe Tech, all rights reserved.
 * Qunhe PROPRIETARY/CONFIDENTIAL, any form of usage is subject to approval.
 */

package exception;


public class ObjectNotFoundException extends Exception{
    private static final String NOT_FOUND = "Nothing found for %s %s";
    private String message;

    public ObjectNotFoundException() {

    }

    public ObjectNotFoundException(final String object, final long identification) {
        message = String.format(NOT_FOUND, object, identification);
    }

    public ObjectNotFoundException(final String object, final String identification) {
        message = String.format(NOT_FOUND, object, identification);
    }


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
