package org.example.action.car.response;

import org.example.action.core.ActionResponse;

/**
 * Response for deleting a car.
 */
public class DeleteCarResponse extends ActionResponse {

    public DeleteCarResponse(boolean success, String message) {
        super(success, message);
    }

    public DeleteCarResponse(boolean success) {
        super(success);
    }

    public static DeleteCarResponse success() {
        return new DeleteCarResponse(true);
    }

    public static DeleteCarResponse notFound(String message) {
        return new DeleteCarResponse(false, message);
    }
} 