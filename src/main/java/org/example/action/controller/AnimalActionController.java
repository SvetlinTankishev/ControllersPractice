package org.example.action.controller;

import org.example.action.animal.request.*;
import org.example.action.animal.response.*;
import org.example.action.core.ActionDispatcher;
import org.example.models.dto.AnimalDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/actions/animals")
public class AnimalActionController {
    private final ActionDispatcher actionDispatcher;

    public AnimalActionController(ActionDispatcher actionDispatcher) {
        this.actionDispatcher = actionDispatcher;
    }

    @PostMapping("/get-all")
    public ResponseEntity<GetAllAnimalsResponse> getAllAnimals() {
        GetAllAnimalsRequest request = new GetAllAnimalsRequest();
        GetAllAnimalsResponse response = actionDispatcher.dispatch(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<GetAnimalByIdResponse> getAnimalById(@RequestBody GetAnimalByIdRequest request) {
        GetAnimalByIdResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CreateAnimalResponse> createAnimal(@Valid @RequestBody AnimalDto dto) {
        CreateAnimalRequest request = new CreateAnimalRequest(dto.getType());
        CreateAnimalResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateAnimalResponse> updateAnimal(@RequestBody UpdateAnimalRequest request) {
        UpdateAnimalResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteAnimalResponse> deleteAnimal(@RequestBody DeleteAnimalRequest request) {
        DeleteAnimalResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<SearchAnimalsResponse> searchAnimals(@RequestBody SearchAnimalsRequest request) {
        SearchAnimalsResponse response = actionDispatcher.dispatch(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-page")
    public ResponseEntity<GetAnimalsPageResponse> getAnimalsPage(@RequestBody GetAnimalsPageRequest request) {
        GetAnimalsPageResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 