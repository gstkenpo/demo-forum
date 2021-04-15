package com.forum.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends Exception {
    private static final long serialVersionUID = 1L;
    private HttpStatus status;
    public ApplicationException(String msg){
        super(msg);
    }

    public ApplicationException(String msg, HttpStatus status){
        super(msg);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return this.status;
    }
}