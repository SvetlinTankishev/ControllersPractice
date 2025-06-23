package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class GetAnimalByIdRequest extends ActionRequest {
    private final Long id;

    public GetAnimalByIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.GET_ANIMAL_BY_ID;
    }
} 