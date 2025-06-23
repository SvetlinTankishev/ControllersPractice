package org.example.action.controller;

import org.example.action.car.request.*;
import org.example.action.car.response.*;
import org.example.action.core.ActionDispatcher;
import org.example.models.dto.CarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controller that exposes action-based endpoints for Car operations.
 * This demonstrates the action-based architectural pattern alongside RESTful.
 */
@RestController
@RequestMapping("/actions/cars")
public class CarActionController {
    private final ActionDispatcher actionDispatcher;

    public CarActionController(ActionDispatcher actionDispatcher) {
        this.actionDispatcher = actionDispatcher;
    }

    @PostMapping("/get-all")
    public ResponseEntity<GetAllCarsResponse> getAllCars() {
        GetAllCarsRequest request = new GetAllCarsRequest();
        GetAllCarsResponse response = actionDispatcher.dispatch(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<GetCarByIdResponse> getCarById(@RequestBody GetCarByIdRequest request) {
        GetCarByIdResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CreateCarResponse> createCar(@Valid @RequestBody CarDto dto) {
        CreateCarRequest request = new CreateCarRequest(dto.getBrand());
        CreateCarResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateCarResponse> updateCar(@RequestBody UpdateCarRequest request) {
        UpdateCarResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteCarResponse> deleteCar(@RequestBody DeleteCarRequest request) {
        DeleteCarResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<SearchCarsResponse> searchCars(@RequestBody SearchCarsRequest request) {
        SearchCarsResponse response = actionDispatcher.dispatch(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-page")
    public ResponseEntity<GetCarsPageResponse> getCarsPage(@RequestBody GetCarsPageRequest request) {
        GetCarsPageResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 