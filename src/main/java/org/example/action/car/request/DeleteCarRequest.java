package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for deleting a car.
 */
public class DeleteCarRequest extends ActionRequest {
    private final Long id;

    public DeleteCarRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getActionType() {
        return CarActionTypes.DELETE_CAR;
    }
} 