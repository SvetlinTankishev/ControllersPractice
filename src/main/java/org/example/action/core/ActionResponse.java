package org.example.action.core;

/**
 * Base class for all action responses in the action-based architecture.
 */
public abstract class ActionResponse {
    private final boolean success;
    private final String message;

    protected ActionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    protected ActionResponse(boolean success) {
        this(success, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
} 