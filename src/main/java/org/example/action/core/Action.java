package org.example.action.core;

/**
 * Base interface for all actions in the action-based architecture.
 * Actions represent discrete operations that can be performed on entities.
 */
public interface Action<TRequest, TResponse> {
    /**
     * Execute the action with the given request and return a response.
     *
     * @param request the action request containing input data
     * @return the action response containing the result
     */
    TResponse execute(TRequest request);

    /**
     * Get the action type identifier.
     *
     * @return the action type as a string
     */
    String getActionType();
} 