package org.example.action.animal.request;

import org.example.action.core.ActionRequest;
import org.example.action.animal.AnimalActionTypes;

public class GetAnimalsPageRequest extends ActionRequest {
    private final int page;
    private final int size;

    public GetAnimalsPageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.GET_ANIMALS_PAGE;
    }
} 