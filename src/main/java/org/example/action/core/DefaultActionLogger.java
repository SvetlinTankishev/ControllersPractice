package org.example.action.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Default implementation of ActionLogger using SLF4J.
 */
@Component
public class DefaultActionLogger implements ActionLogger {
    private static final Logger logger = LoggerFactory.getLogger(DefaultActionLogger.class);

    @Override
    public void logActionStart(String actionType, ActionRequest request) {
        logger.info("Starting action execution: actionType={}, requestClass={}", 
                   actionType, request.getClass().getSimpleName());
    }

    @Override
    public void logActionSuccess(String actionType, ActionRequest request, ActionResponse response, long executionTimeMs) {
        logger.info("Action executed successfully: actionType={}, requestClass={}, responseClass={}, executionTime={}ms", 
                   actionType, request.getClass().getSimpleName(), response.getClass().getSimpleName(), executionTimeMs);
    }

    @Override
    public void logActionFailure(String actionType, ActionRequest request, Exception error, long executionTimeMs) {
        logger.error("Action execution failed: actionType={}, requestClass={}, executionTime={}ms, error={}", 
                    actionType, request.getClass().getSimpleName(), executionTimeMs, error.getMessage(), error);
    }

    @Override
    public void logValidationFailure(String actionType, ActionRequest request, ValidationResult validationResult) {
        logger.warn("Action validation failed: actionType={}, requestClass={}, errors={}", 
                   actionType, request.getClass().getSimpleName(), validationResult.getErrors());
    }
} 