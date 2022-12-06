package com.dcagon.decapay.exception;

public class NotFoundException extends RuntimeException {

    private String message;

    public NotFoundException() {
        super();
        this.message = "Not found";
    }

    ;

    public NotFoundException(String msg) {
        super(msg);
        this.message = msg;
    }
}

