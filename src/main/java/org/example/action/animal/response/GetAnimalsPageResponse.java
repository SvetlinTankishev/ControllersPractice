package org.example.action.animal.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.Animal;
import java.util.List;

public class GetAnimalsPageResponse extends ActionResponse {
    private final List<Animal> animals;

    public GetAnimalsPageResponse(List<Animal> animals) {
        super(true);
        this.animals = animals;
    }

    public GetAnimalsPageResponse(String message) {
        super(false, message);
        this.animals = null;
    }

    public static GetAnimalsPageResponse invalidParameters(String message) {
        return new GetAnimalsPageResponse(message);
    }

    public List<Animal> getAnimals() {
        return animals;
    }
} 