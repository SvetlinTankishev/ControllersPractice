package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.GetCarByIdRequest;
import org.example.action.car.response.GetCarByIdResponse;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.springframework.stereotype.Component;
import java.util.Optional;

/**
 * Action for getting a car by ID.
 */
@Component
public class GetCarByIdAction implements Action<GetCarByIdRequest, GetCarByIdResponse> {
    private final CarService carService;

    public GetCarByIdAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public GetCarByIdResponse execute(GetCarByIdRequest request) {
        Optional<Car> car = carService.getById(request.getId());
        if (car.isPresent()) {
            return new GetCarByIdResponse(car.get());
        } else {
            return GetCarByIdResponse.notFound("Car not found with id: " + request.getId());
        }
    }

    @Override
    public String getActionType() {
        return CarActionTypes.GET_CAR_BY_ID;
    }
} 