package com.livedoc.livedoc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("E001", "Email already registered"),
    INVALID_CREDENTIALS("E002", "Invalid email or password"),
    USER_NOT_FOUND("E003", "User not found"),
    REFRESH_TOKEN_EXPIRED("E004", "Refresh token expired"),
    TOKEN_INVALID("E005", "Invalid token"),
    GENERAL_ERROR("E999", "Something went wrong");

    private final String code;
    private final String description;
}
