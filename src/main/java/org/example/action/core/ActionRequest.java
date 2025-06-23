package org.example.action.core;

/**
 * Base class for all action requests in the action-based architecture.
 */
public abstract class ActionRequest {
    
    /**
     * Get the action type for this request.
     *
     * @return the action type as a string
     */
    public abstract String getActionType();
} 