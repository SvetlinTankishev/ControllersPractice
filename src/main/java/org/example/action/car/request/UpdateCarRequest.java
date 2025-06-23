package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for updating a car.
 */
public class UpdateCarRequest extends ActionRequest {
    private final Long id;
    private final String brand;

    public UpdateCarRequest(Long id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String getActionType() {
        return CarActionTypes.UPDATE_CAR;
    }
} 