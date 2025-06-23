package org.example.action.car;

import org.example.action.core.Action;
import org.example.action.car.request.SearchCarsRequest;
import org.example.action.car.response.SearchCarsResponse;
import org.example.service.CarService;
import org.springframework.stereotype.Component;

/**
 * Action for searching cars by brand.
 */
@Component
public class SearchCarsAction implements Action<SearchCarsRequest, SearchCarsResponse> {
    private final CarService carService;

    public SearchCarsAction(CarService carService) {
        this.carService = carService;
    }

    @Override
    public SearchCarsResponse execute(SearchCarsRequest request) {
        return new SearchCarsResponse(carService.searchByBrand(request.getBrand()));
    }

    @Override
    public String getActionType() {
        return CarActionTypes.SEARCH_CARS;
    }
} 