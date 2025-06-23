package org.example.action.animal.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Animal;

public class UpdateAnimalResponse extends ActionResponse {
    private final Animal animal;

    public UpdateAnimalResponse(Animal animal) {
        super(true);
        this.animal = animal;
    }

    public UpdateAnimalResponse(String message) {
        super(false, message);
        this.animal = null;
    }

    public static UpdateAnimalResponse notFound(String message) {
        return new UpdateAnimalResponse(message);
    }

    public Animal getAnimal() {
        return animal;
    }
} 