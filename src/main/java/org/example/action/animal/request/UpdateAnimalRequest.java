package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class UpdateAnimalRequest extends ActionRequest {
    private final Long id;
    private final String type;

    public UpdateAnimalRequest(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.UPDATE_ANIMAL;
    }
} 