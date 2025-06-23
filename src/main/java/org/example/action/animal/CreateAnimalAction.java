package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.CreateAnimalRequest;
import org.example.action.animal.response.CreateAnimalResponse;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;

@Component
public class CreateAnimalAction implements Action<CreateAnimalRequest, CreateAnimalResponse> {
    private final AnimalService animalService;

    public CreateAnimalAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public CreateAnimalResponse execute(CreateAnimalRequest request) {
        try {
            Animal createdAnimal = animalService.add(request.getType());
            return new CreateAnimalResponse(createdAnimal);
        } catch (Exception e) {
            return new CreateAnimalResponse("Failed to create animal: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.CREATE_ANIMAL;
    }
} 