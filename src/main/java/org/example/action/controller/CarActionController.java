package org.example.action.controller;

import org.example.action.entity.Car;
import org.example.action.dto.CarDto;
import org.example.action.service.CarService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/action/car")
public class CarActionController {
    private final CarService carService = new CarService();

    @GetMapping("/list")
    public List<Car> listCars() {
        return carService.getAll();
    }

    @GetMapping("/get")
    public Car getCar(@RequestParam Long id) {
        Car car = carService.getById(id);
        if (car == null) throw new NoSuchElementException("Car not found");
        return car;
    }

    @PostMapping("/add")
    public Car addCar(@RequestBody CarDto dto) {
        return carService.add(dto.getBrand());
    }

    @PostMapping("/remove")
    public void removeCar(@RequestParam Long id) {
        carService.delete(id);
    }
} 