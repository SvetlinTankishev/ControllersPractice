package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for creating a new car.
 */
public class CreateCarRequest extends ActionRequest {
    private final String brand;

    public CreateCarRequest(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String getActionType() {
        return CarActionTypes.CREATE_CAR;
    }
} 