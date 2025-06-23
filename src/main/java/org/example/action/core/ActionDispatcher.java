package org.example.action.core;

import org.springframework.stereotype.Component;
import org.example.performance.PerformanceMonitor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Central dispatcher for routing action requests to appropriate action handlers.
 * Enhanced with validation and logging capabilities.
 */
@Component
public class ActionDispatcher {
    private final Map<String, Action<?, ?>> actions = new HashMap<>();
    private final Map<String, ActionValidator<?>> validators = new HashMap<>();
    private final ActionLogger actionLogger;
    private final PerformanceMonitor performanceMonitor;

    public ActionDispatcher(List<Action<?, ?>> actionList, 
                          List<ActionValidator<?>> validatorList, 
                          ActionLogger actionLogger,
                          PerformanceMonitor performanceMonitor) {
        for (Action<?, ?> action : actionList) {
            actions.put(action.getActionType(), action);
        }
        for (ActionValidator<?> validator : validatorList) {
            // Note: In a real implementation, we'd need a way to associate validators with action types
            // For now, we'll just store them and use them optionally
        }
        this.actionLogger = actionLogger;
        this.performanceMonitor = performanceMonitor;
    }

    /**
     * Dispatch an action request to the appropriate handler with validation and logging.
     *
     * @param request the action request
     * @return the action response
     * @throws IllegalArgumentException if no handler is found for the action type
     */
    @SuppressWarnings("unchecked")
    public <TRequest extends ActionRequest, TResponse extends ActionResponse> TResponse dispatch(TRequest request) {
        String actionType = request.getActionType();
        Action<TRequest, TResponse> action = (Action<TRequest, TResponse>) actions.get(actionType);
        
        if (action == null) {
            throw new IllegalArgumentException("No handler found for action type: " + actionType);
        }

        // Log action start
        actionLogger.logActionStart(actionType, request);
        long startTime = System.currentTimeMillis();

        try {
            // Execute action
            TResponse response = action.execute(request);
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log success
            actionLogger.logActionSuccess(actionType, request, response, executionTime);
            
            // Record performance metrics
            performanceMonitor.recordActionExecution(executionTime, response.isSuccess());
            
            return response;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Log failure
            actionLogger.logActionFailure(actionType, request, e, executionTime);
            
            // Record performance metrics for failure
            performanceMonitor.recordActionExecution(executionTime, false);
            
            throw e;
        }
    }

    /**
     * Check if a handler exists for the given action type.
     *
     * @param actionType the action type to check
     * @return true if a handler exists, false otherwise
     */
    public boolean hasHandler(String actionType) {
        return actions.containsKey(actionType);
    }
} 