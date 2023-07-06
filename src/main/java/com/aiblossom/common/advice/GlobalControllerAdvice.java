package com.aiblossom.common.advice;

import com.aiblossom.common.Exception.BlossomErrorCode;
import com.aiblossom.common.Exception.BlossomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(value = BlossomException.class)
    public ResponseEntity<String> exception(BlossomException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exception(Exception e){
        return ResponseEntity.status(403).body(e.getMessage());
    }
}
