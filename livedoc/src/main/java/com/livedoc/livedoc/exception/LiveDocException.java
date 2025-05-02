package com.livedoc.livedoc.exception;

import lombok.Getter;

@Getter
public class LiveDocException extends RuntimeException {
    
    private final ErrorCode errorCode;

    public LiveDocException(ErrorCode errorCode){
        super(errorCode.getDescription());
        this.errorCode = errorCode;
    }
}
