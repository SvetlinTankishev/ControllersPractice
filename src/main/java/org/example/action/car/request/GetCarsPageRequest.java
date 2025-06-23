package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for getting a page of cars.
 */
public class GetCarsPageRequest extends ActionRequest {
    private final int page;
    private final int size;

    public GetCarsPageRequest(int page, int size) {
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
        return CarActionTypes.GET_CARS_PAGE;
    }
} 