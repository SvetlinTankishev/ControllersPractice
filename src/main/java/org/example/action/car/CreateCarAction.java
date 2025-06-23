package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.CreateCarRequest;
import org.example.action.car.response.CreateCarResponse;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.springframework.stereotype.Component;

/**
 * Action for creating a new car.
 */
@Component
public class CreateCarAction implements Action<CreateCarRequest, CreateCarResponse> {
    private final CarService carService;

    public CreateCarAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public CreateCarResponse execute(CreateCarRequest request) {
        try {
            Car createdCar = carService.add(request.getBrand());
            return new CreateCarResponse(createdCar);
        } catch (Exception e) {
            return new CreateCarResponse("Failed to create car: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return CarActionTypes.CREATE_CAR;
    }
} 