package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for searching cars by brand.
 */
public class SearchCarsRequest extends ActionRequest {
    private final String brand;

    public SearchCarsRequest(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String getActionType() {
        return CarActionTypes.SEARCH_CARS;
    }
} 