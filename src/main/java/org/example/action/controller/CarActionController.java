package org.example.action.controller;

import org.example.models.entity.Car;
import org.example.models.dto.CarDto;
import org.example.service.CarService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/action/car")
public class CarActionController {
    private final CarService carService;

    public CarActionController(CarService carService) {
        this.carService = carService;
    }

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

    @PostMapping("/update")
    public Car updateCar(@RequestParam Long id, @RequestBody CarDto dto) {
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