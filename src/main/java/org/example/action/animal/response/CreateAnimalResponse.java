package org.example.action.animal.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Animal;

public class CreateAnimalResponse extends ActionResponse {
    private final Animal animal;

    public CreateAnimalResponse(Animal animal) {
        super(true);
        this.animal = animal;
    }

    public CreateAnimalResponse(String message) {
        super(false, message);
        this.animal = null;
    }

    public Animal getAnimal() {
        return animal;
    }
} 