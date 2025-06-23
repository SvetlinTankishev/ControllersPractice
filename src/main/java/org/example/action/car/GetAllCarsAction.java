package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.GetAllCarsRequest;
import org.example.action.car.response.GetAllCarsResponse;
import org.example.service.CarService;
import org.springframework.stereotype.Component;

/**
 * Action for getting all cars.
 */
@Component
public class GetAllCarsAction implements Action<GetAllCarsRequest, GetAllCarsResponse> {
    private final CarService carService;

    public GetAllCarsAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public GetAllCarsResponse execute(GetAllCarsRequest request) {
        return new GetAllCarsResponse(carService.getAll());
    }

    @Override
    public String getActionType() {
        return CarActionTypes.GET_ALL_CARS;
    }
} 