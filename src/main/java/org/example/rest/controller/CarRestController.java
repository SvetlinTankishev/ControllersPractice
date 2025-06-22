package org.example.rest.controller;

import org.example.models.entity.Car;
import org.example.models.dto.CarDto;
import org.example.service.CarService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/cars")
public class CarRestController {
    private final CarService carService;

    public CarRestController(CarService carService) {
        this.carService = carService;
    }

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

    @PatchMapping("/{id}")
    public Car patchCar(@PathVariable Long id, @RequestBody CarDto dto) {
        Car car = carService.getById(id);
        if (car == null) throw new NoSuchElementException("Car not found");
        if (dto.getBrand() != null) {
            car.setBrand(dto.getBrand());
        }
        // In a real app, persist the change here
        return car;
    }

    @GetMapping("/search")
    public List<Car> searchCars(@RequestParam(required = false) String brand) {
        return carService.searchByBrand(brand);
    }

    @GetMapping("/page")
    public List<Car> getCarsPage(@RequestParam int page, @RequestParam int size) {
        return carService.getPage(page, size);
    }
} 