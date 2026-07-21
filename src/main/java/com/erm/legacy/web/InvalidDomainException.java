package com.erm.legacy.web;

import java.util.List;

public class InvalidDomainException extends RuntimeException {

    private final String field;
    private final String rejectedValue;
    private final List<String> allowedValues;

    public InvalidDomainException(String field, String rejectedValue, List<String> allowedValues) {
        super("Invalid domain value '" + rejectedValue + "'. Allowed values: " + allowedValues);
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.allowedValues = allowedValues;
    }

    public String getField() {
        return field;
    }

    public String getRejectedValue() {
        return rejectedValue;
    }

    public List<String> getAllowedValues() {
        return allowedValues;
    }
}
