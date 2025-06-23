package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.GetCarsPageRequest;
import org.example.action.car.response.GetCarsPageResponse;
import org.example.service.CarService;
import org.springframework.stereotype.Component;

/**
 * Action for getting a page of cars.
 */
@Component
public class GetCarsPageAction implements Action<GetCarsPageRequest, GetCarsPageResponse> {
    private final CarService carService;

    public GetCarsPageAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public GetCarsPageResponse execute(GetCarsPageRequest request) {
        if (request.getPage() < 0 || request.getSize() <= 0) {
            return GetCarsPageResponse.invalidParameters("Page must be >= 0 and size must be > 0");
        }
        
        return new GetCarsPageResponse(carService.getPage(request.getPage(), request.getSize()));
    }

    @Override
    public String getActionType() {
        return CarActionTypes.GET_CARS_PAGE;
    }
} 