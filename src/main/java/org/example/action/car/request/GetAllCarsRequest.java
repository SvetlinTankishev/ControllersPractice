package org.example.action.car.request;

import org.example.action.core.ActionRequest;
import org.example.action.car.CarActionTypes;

/**
 * Request for getting all cars.
 */
public class GetAllCarsRequest extends ActionRequest {
    
    @Override
    public String getActionType() {
        return CarActionTypes.GET_ALL_CARS;
    }
} 