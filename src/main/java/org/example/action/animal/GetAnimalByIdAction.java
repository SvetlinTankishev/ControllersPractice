package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.GetAnimalByIdRequest;
import org.example.action.animal.response.GetAnimalByIdResponse;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class GetAnimalByIdAction implements Action<GetAnimalByIdRequest, GetAnimalByIdResponse> {
    private final AnimalService animalService;

    public GetAnimalByIdAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public GetAnimalByIdResponse execute(GetAnimalByIdRequest request) {
        Optional<Animal> animal = animalService.getById(request.getId());
        if (animal.isPresent()) {
            return new GetAnimalByIdResponse(animal.get());
        } else {
            return GetAnimalByIdResponse.notFound("Animal not found with id: " + request.getId());
        }
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.GET_ANIMAL_BY_ID;
    }
} 