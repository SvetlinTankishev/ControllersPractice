package org.example.service;

import org.example.models.entity.Car;
import org.example.repository.CarRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {
    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Optional<Car> getById(Long id) {
        return carRepository.findById(id);
    }

    public Car add(String brand) {
        Car car = new Car();
        car.setBrand(brand);
        return carRepository.save(car);
    }

    public boolean delete(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Car> searchByBrand(String brand) {
        if (brand == null || brand.isEmpty()) return carRepository.findAll();
        return carRepository.findByBrandContainingIgnoreCase(brand);
    }

    public List<Car> getPage(int page, int size) {
        if (size <= 0) return List.of();
        return carRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size)).getContent();
    }

    public Optional<Car> update(Long id, String brand) {
        Optional<Car> carOpt = getById(id);
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setBrand(brand);
            return Optional.of(carRepository.save(car));
        }
        return Optional.empty();
    }
} 