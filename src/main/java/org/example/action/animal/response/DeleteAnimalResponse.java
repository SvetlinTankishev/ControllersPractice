package org.example.action.animal.response;

import org.example.action.core.ActionResponse;

public class DeleteAnimalResponse extends ActionResponse {

    public DeleteAnimalResponse(boolean success, String message) {
        super(success, message);
    }

    public DeleteAnimalResponse(boolean success) {
        super(success);
    }

    public static DeleteAnimalResponse success() {
        return new DeleteAnimalResponse(true, "Animal deleted successfully");
    }

    public static DeleteAnimalResponse notFound(String message) {
        return new DeleteAnimalResponse(false, message);
    }
} 