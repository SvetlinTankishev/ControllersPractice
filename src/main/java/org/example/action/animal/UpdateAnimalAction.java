package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.UpdateAnimalRequest;
import org.example.action.animal.response.UpdateAnimalResponse;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UpdateAnimalAction implements Action<UpdateAnimalRequest, UpdateAnimalResponse> {
    private final AnimalService animalService;

    public UpdateAnimalAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public UpdateAnimalResponse execute(UpdateAnimalRequest request) {
        try {
            Optional<Animal> updatedAnimal = animalService.update(request.getId(), request.getType());
            if (updatedAnimal.isPresent()) {
                return new UpdateAnimalResponse(updatedAnimal.get());
            } else {
                return UpdateAnimalResponse.notFound("Animal not found with id: " + request.getId());
            }
        } catch (Exception e) {
            return new UpdateAnimalResponse("Failed to update animal: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.UPDATE_ANIMAL;
    }
} 