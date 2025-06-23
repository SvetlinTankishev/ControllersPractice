package org.example.action.car.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Car;

/**
 * Response for creating a car.
 */
public class CreateCarResponse extends ActionResponse {
    private final Car car;

    public CreateCarResponse(Car car) {
        super(true);
        this.car = car;
    }

    public CreateCarResponse(String message) {
        super(false, message);
        this.car = null;
    }

    public Car getCar() {
        return car;
    }
} 