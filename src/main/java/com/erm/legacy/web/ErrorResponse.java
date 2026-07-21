package com.erm.legacy.web;

public class ErrorResponse {

    private final String code;
    private final String message;
    private final String field;
    private final String path;

    public ErrorResponse(String code, String message, String field, String path) {
        this.code = code;
        this.message = message;
        this.field = field;
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getField() {
        return field;
    }

    public String getPath() {
        return path;
    }
}
