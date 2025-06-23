package org.example.action.animal.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Animal;
import java.util.List;

public class GetAllAnimalsResponse extends ActionResponse {
    private final List<Animal> animals;

    public GetAllAnimalsResponse(List<Animal> animals) {
        super(true);
        this.animals = animals;
    }

    public GetAllAnimalsResponse(String message) {
        super(false, message);
        this.animals = null;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
} 