package org.example.rest.controller;

import org.example.models.entity.Car;
import org.example.models.dto.CarDto;
import org.example.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class CarRestController {
    private final CarService carService;

    public CarRestController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars() {
        return ResponseEntity.ok(carService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable("id") Long id) {
        Optional<Car> car = carService.getById(id);
        return car.map(ResponseEntity::ok)
                  .orElseThrow(() -> new NoSuchElementException("Car not found with id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Car> addCar(@Valid @RequestBody CarDto dto) {
        Car createdCar = carService.add(dto.getBrand());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        boolean deleted = carService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NoSuchElementException("Car not found with id: " + id);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Car> patchCar(@PathVariable("id") Long id, @Valid @RequestBody CarDto dto) {
        if (dto.getBrand() != null) {
            Optional<Car> updatedCar = carService.update(id, dto.getBrand());
            return updatedCar.map(ResponseEntity::ok)
                            .orElseThrow(() -> new NoSuchElementException("Car not found with id: " + id));
        }
        Optional<Car> car = carService.getById(id);
        return car.map(ResponseEntity::ok)
                  .orElseThrow(() -> new NoSuchElementException("Car not found with id: " + id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Car>> searchCars(@RequestParam(value = "brand", required = false) String brand) {
        return ResponseEntity.ok(carService.searchByBrand(brand));
    }

    @GetMapping("/page")
    public ResponseEntity<List<Car>> getCarsPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
        }
        return ResponseEntity.ok(carService.getPage(page, size));
    }
} 