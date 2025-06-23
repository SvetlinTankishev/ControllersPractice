package org.example.action.animal.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Animal;

public class GetAnimalByIdResponse extends ActionResponse {
    private final Animal animal;

    public GetAnimalByIdResponse(Animal animal, boolean success, String message) {
        super(success, message);
        this.animal = animal;
    }

    public GetAnimalByIdResponse(Animal animal) {
        super(true);
        this.animal = animal;
    }

    public static GetAnimalByIdResponse notFound(String message) {
        return new GetAnimalByIdResponse(null, false, message);
    }

    public Animal getAnimal() {
        return animal;
    }
} 