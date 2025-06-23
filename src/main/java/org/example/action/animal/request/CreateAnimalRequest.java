package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class CreateAnimalRequest extends ActionRequest {
    private final String type;

    public CreateAnimalRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.CREATE_ANIMAL;
    }
} 