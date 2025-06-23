package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.DeleteCarRequest;
import org.example.action.car.response.DeleteCarResponse;
import org.example.service.CarService;
import org.springframework.stereotype.Component;

/**
 * Action for deleting a car.
 */
@Component
public class DeleteCarAction implements Action<DeleteCarRequest, DeleteCarResponse> {
    private final CarService carService;

    public DeleteCarAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public DeleteCarResponse execute(DeleteCarRequest request) {
        boolean deleted = carService.delete(request.getId());
        if (deleted) {
            return DeleteCarResponse.success();
        } else {
            return DeleteCarResponse.notFound("Car not found with id: " + request.getId());
        }
    }

    @Override
    public String getActionType() {
        return CarActionTypes.DELETE_CAR;
    }
} 