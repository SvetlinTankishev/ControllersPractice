package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class GetAllAnimalsRequest extends ActionRequest {
    @Override
    public String getActionType() {
        return AnimalActionTypes.GET_ALL_ANIMALS;
    }
} 