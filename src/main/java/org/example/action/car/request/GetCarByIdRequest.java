package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for getting a car by ID.
 */
public class GetCarByIdRequest extends ActionRequest {
    private final Long id;

    public GetCarByIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getActionType() {
        return CarActionTypes.GET_CAR_BY_ID;
    }
} 