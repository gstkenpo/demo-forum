package com.forum.exception.handler;

import java.util.List;
import java.util.stream.Collectors;

import com.forum.dto.ForumError;

import org.jboss.logging.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler{
    private Logger logger = Logger.getLogger(this.getClass());
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<List<ForumError>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    //     logger.debug("In handleValidationExceptions");
        
    //     List<ForumError> result = ex.getBindingResult()
    //             .getAllErrors()
    //             .stream()
    //             .map(e -> new ForumError(((FieldError) e).getField(), e.getDefaultMessage()))
    //             .collect(Collectors.toList());
    //     return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    // }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.debug("In handleValidationExceptions");
        List<ForumError> body = ex.getBindingResult()
                                    .getAllErrors()
                                    .stream()
                                    .map(e -> new ForumError(((FieldError) e).getField(), e.getDefaultMessage()))
                                    .collect(Collectors.toList());
        return handleExceptionInternal(ex, body, headers, status, request);
    }
}