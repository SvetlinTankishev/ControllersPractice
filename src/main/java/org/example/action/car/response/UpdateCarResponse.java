package org.example.action.car.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Car;

/**
 * Response for updating a car.
 */
public class UpdateCarResponse extends ActionResponse {
    private final Car car;

    public UpdateCarResponse(Car car) {
        super(true);
        this.car = car;
    }

    public UpdateCarResponse(String message) {
        super(false, message);
        this.car = null;
    }

    public static UpdateCarResponse notFound(String message) {
        return new UpdateCarResponse(message);
    }

    public Car getCar() {
        return car;
    }
} 