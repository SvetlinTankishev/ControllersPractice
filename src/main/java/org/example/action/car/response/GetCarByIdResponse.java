package org.example.action.car.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Car;

/**
 * Response for getting a car by ID.
 */
public class GetCarByIdResponse extends ActionResponse {
    private final Car car;

    public GetCarByIdResponse(Car car, boolean success, String message) {
        super(success, message);
        this.car = car;
    }

    public GetCarByIdResponse(Car car) {
        super(true);
        this.car = car;
    }

    public static GetCarByIdResponse notFound(String message) {
        return new GetCarByIdResponse(null, false, message);
    }

    public Car getCar() {
        return car;
    }
} 