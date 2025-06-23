package org.example.action.core;

/**
 * Interface for logging action execution details.
 */
public interface ActionLogger {
    
    /**
     * Logs the start of action execution.
     *
     * @param actionType the type of action being executed
     * @param request the action request
     */
    void logActionStart(String actionType, ActionRequest request);
    
    /**
     * Logs successful action completion.
     *
     * @param actionType the type of action executed
     * @param request the action request
     * @param response the action response
     * @param executionTimeMs execution time in milliseconds
     */
    void logActionSuccess(String actionType, ActionRequest request, ActionResponse response, long executionTimeMs);
    
    /**
     * Logs action failure.
     *
     * @param actionType the type of action executed
     * @param request the action request
     * @param error the error that occurred
     * @param executionTimeMs execution time in milliseconds
     */
    void logActionFailure(String actionType, ActionRequest request, Exception error, long executionTimeMs);
    
    /**
     * Logs validation failure.
     *
     * @param actionType the type of action
     * @param request the action request
     * @param validationResult the validation result
     */
    void logValidationFailure(String actionType, ActionRequest request, ValidationResult validationResult);
} 