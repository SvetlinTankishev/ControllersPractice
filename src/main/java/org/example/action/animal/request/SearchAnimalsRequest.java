package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class SearchAnimalsRequest extends ActionRequest {
    private final String type;

    public SearchAnimalsRequest(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.SEARCH_ANIMALS;
    }
} 