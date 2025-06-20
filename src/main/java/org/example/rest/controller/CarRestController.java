package org.example.rest.controller;

import org.example.rest.entity.Car;
import org.example.rest.dto.CarDto;
import org.example.rest.service.CarService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/cars")
public class CarRestController {
    private final CarService carService = new CarService();

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAll();
    }

    @GetMapping("/{id}")
    public Car getCar(@PathVariable Long id) {
        Car car = carService.getById(id);
        if (car == null) throw new NoSuchElementException("Car not found");
        return car;
    }

    @PostMapping
    public Car addCar(@RequestBody CarDto dto) {
        return carService.add(dto.getBrand());
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.delete(id);
    }
} 