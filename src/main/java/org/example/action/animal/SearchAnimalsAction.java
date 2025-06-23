package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.SearchAnimalsRequest;
import org.example.action.animal.response.SearchAnimalsResponse;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;

@Component
public class SearchAnimalsAction implements Action<SearchAnimalsRequest, SearchAnimalsResponse> {
    private final AnimalService animalService;

    public SearchAnimalsAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public SearchAnimalsResponse execute(SearchAnimalsRequest request) {
        return new SearchAnimalsResponse(animalService.searchByType(request.getType()));
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.SEARCH_ANIMALS;
    }
} 