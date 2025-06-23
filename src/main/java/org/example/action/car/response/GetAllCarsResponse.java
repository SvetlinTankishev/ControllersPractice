package org.example.action.car.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Car;
import java.util.List;

/**
 * Response for getting all cars.
 */
public class GetAllCarsResponse extends ActionResponse {
    private final List<Car> cars;

    public GetAllCarsResponse(List<Car> cars) {
        super(true);
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }
} 