package org.example.action.animal;

import org.example.action.core.Action;
import org.example.action.animal.request.GetAnimalsPageRequest;
import org.example.action.animal.response.GetAnimalsPageResponse;
import org.example.service.AnimalService;
import org.springframework.stereotype.Component;

@Component
public class GetAnimalsPageAction implements Action<GetAnimalsPageRequest, GetAnimalsPageResponse> {
    private final AnimalService animalService;

    public GetAnimalsPageAction(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Override
    public GetAnimalsPageResponse execute(GetAnimalsPageRequest request) {
        if (request.getPage() < 0 || request.getSize() <= 0) {
            return GetAnimalsPageResponse.invalidParameters("Page must be >= 0 and size must be > 0");
        }
        
        return new GetAnimalsPageResponse(animalService.getPage(request.getPage(), request.getSize()));
    }

    @Override
    public String getActionType() {
        return AnimalActionTypes.GET_ANIMALS_PAGE;
    }
} 