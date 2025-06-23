package org.example.action.car.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Car;
import java.util.List;

/**
 * Response for getting a page of cars.
 */
public class GetCarsPageResponse extends ActionResponse {
    private final List<Car> cars;

    public GetCarsPageResponse(List<Car> cars) {
        super(true);
        this.cars = cars;
    }

    public GetCarsPageResponse(String message) {
        super(false, message);
        this.cars = null;
    }

    public static GetCarsPageResponse invalidParameters(String message) {
        return new GetCarsPageResponse(message);
    }

    public List<Car> getCars() {
        return cars;
    }
} 