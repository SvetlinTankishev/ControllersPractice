package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.DeleteAnimalRequest;
import org.example.action.animal.response.DeleteAnimalResponse;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;

@Component
public class DeleteAnimalAction implements Action<DeleteAnimalRequest, DeleteAnimalResponse> {
    private final AnimalService animalService;

    public DeleteAnimalAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public DeleteAnimalResponse execute(DeleteAnimalRequest request) {
        boolean deleted = animalService.delete(request.getId());
        if (deleted) {
            return DeleteAnimalResponse.success();
        } else {
            return DeleteAnimalResponse.notFound("Animal not found with id: " + request.getId());
        }
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.DELETE_ANIMAL;
    }
} 