package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.UpdateCarRequest;
import org.example.action.car.response.UpdateCarResponse;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Action for updating a car.
 */
@Component
public class UpdateCarAction implements Action<UpdateCarRequest, UpdateCarResponse> {
    private final CarService carService;

    public UpdateCarAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public UpdateCarResponse execute(UpdateCarRequest request) {
        try {
            Optional<Car> updatedCar = carService.update(request.getId(), request.getBrand());
            if (updatedCar.isPresent()) {
                return new UpdateCarResponse(updatedCar.get());
            } else {
                return UpdateCarResponse.notFound("Car not found with id: " + request.getId());
            }
        } catch (Exception e) {
            return new UpdateCarResponse("Failed to update car: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return CarActionTypes.UPDATE_CAR;
    }
} 