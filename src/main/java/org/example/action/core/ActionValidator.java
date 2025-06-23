package org.example.action.core;

/**
 * Interface for validating action requests before execution.
 */
public interface ActionValidator<TRequest extends ActionRequest> {
    
    /**
     * Validates the action request.
     *
     * @param request the request to validate
     * @return ValidationResult containing validation outcome
     */
    ValidationResult validate(TRequest request);
} 