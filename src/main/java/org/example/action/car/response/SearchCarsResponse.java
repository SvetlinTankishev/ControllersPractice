package org.example.action.car.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Car;
import java.util.List;

/**
 * Response for searching cars.
 */
public class SearchCarsResponse extends ActionResponse {
    private final List<Car> cars;

    public SearchCarsResponse(List<Car> cars) {
        super(true);
        this.cars = cars;
    }

    public List<Car> getCars() {
        return cars;
    }
} 