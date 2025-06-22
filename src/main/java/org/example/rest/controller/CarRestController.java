package org.example.rest.controller;

import org.example.models.entity.Car;
import org.example.models.dto.CarDto;
import org.example.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Car getCar(@PathVariable("id") Long id) {
        Car car = carService.getById(id);
        if (car == null) throw new NoSuchElementException("Car not found");
        return car;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Car addCar(@RequestBody CarDto dto) {
        return carService.add(dto.getBrand());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable("id") Long id) {
        boolean deleted = carService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public Car patchCar(@PathVariable("id") Long id, @RequestBody CarDto dto) {
        if (dto.getBrand() != null) {
            Car updatedCar = carService.update(id, dto.getBrand());
            if (updatedCar == null) {
                throw new NoSuchElementException("Car not found");
            }
            return updatedCar;
        }
        return carService.getById(id);
    }

    @GetMapping("/search")
    public List<Car> searchCars(@RequestParam(value = "brand", required = false) String brand) {
        return carService.searchByBrand(brand);
    }

    @GetMapping("/page")
    public List<Car> getCarsPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        return carService.getPage(page, size);
    }
} 