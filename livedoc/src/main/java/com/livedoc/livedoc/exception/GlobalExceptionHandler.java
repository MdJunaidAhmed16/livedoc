package com.livedoc.livedoc.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LiveDocException.class)
    public ResponseEntity<Map<String, Object>> handleLiveDocException(LiveDocException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now().toString());
        body.put("errorCode", errorCode.getCode());
        body.put("errorMessage", errorCode.getDescription());

        HttpStatus status = mapErrorCodeToHttpStatus(errorCode);

        return new ResponseEntity<>(body, status);
    }

    private HttpStatus mapErrorCodeToHttpStatus(ErrorCode code) {
        return switch (code) {
            case EMAIL_ALREADY_EXISTS -> HttpStatus.CONFLICT;
            case INVALID_CREDENTIALS, USER_NOT_FOUND -> HttpStatus.UNAUTHORIZED;
            case REFRESH_TOKEN_EXPIRED, TOKEN_INVALID -> HttpStatus.UNAUTHORIZED;
            case GENERAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
