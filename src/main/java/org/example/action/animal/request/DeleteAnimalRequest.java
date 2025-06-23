package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class DeleteAnimalRequest extends ActionRequest {
    private final Long id;

    public DeleteAnimalRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.DELETE_ANIMAL;
    }
} 