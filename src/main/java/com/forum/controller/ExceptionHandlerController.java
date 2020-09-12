package com.forum.controller;

import com.forum.exception.ApplicationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> applicationExceptionHandler(ApplicationException e){
        logger.error("Application Exception Handler is trigger: " + e);
	    return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    } 
}
