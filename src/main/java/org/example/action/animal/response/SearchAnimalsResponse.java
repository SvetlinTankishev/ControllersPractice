package org.example.action.animal.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Animal;
import java.util.List;

public class SearchAnimalsResponse extends ActionResponse {
    private final List<Animal> animals;

    public SearchAnimalsResponse(List<Animal> animals) {
        super(true);
        this.animals = animals;
    }

    public List<Animal> getAnimals() {
        return animals;
    }
} 