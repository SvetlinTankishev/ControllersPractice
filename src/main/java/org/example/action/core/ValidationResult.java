package org.example.action.core;

import java.util.List;
import java.util.ArrayList;

/**
 * Result of action request validation.
 */
public class ValidationResult {
    private final boolean isValid;
    private final List<String> errors;

    private ValidationResult(boolean isValid, List<String> errors) {
        this.isValid = isValid;
        this.errors = errors != null ? new ArrayList<>(errors) : new ArrayList<>();
    }

    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    public static ValidationResult failure(String error) {
        List<String> errors = new ArrayList<>();
        errors.add(error);
        return new ValidationResult(false, errors);
    }

    public static ValidationResult failure(List<String> errors) {
        return new ValidationResult(false, errors);
    }

    public boolean isValid() {
        return isValid;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public String getFirstError() {
        return errors.isEmpty() ? null : errors.get(0);
    }
} 