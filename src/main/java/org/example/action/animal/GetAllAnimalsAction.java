package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.GetAllAnimalsRequest;
import org.example.action.animal.response.GetAllAnimalsResponse;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;

@Component
public class GetAllAnimalsAction implements Action<GetAllAnimalsRequest, GetAllAnimalsResponse> {
    private final AnimalService animalService;

    public GetAllAnimalsAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public GetAllAnimalsResponse execute(GetAllAnimalsRequest request) {
        try {
            return new GetAllAnimalsResponse(animalService.getAll());
        } catch (Exception e) {
            return new GetAllAnimalsResponse(e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.GET_ALL_ANIMALS;
    }
} 