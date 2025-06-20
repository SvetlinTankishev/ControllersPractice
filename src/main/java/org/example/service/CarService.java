package org.example.service;

import org.example.models.entity.Car;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class CarService {
    private final List<Car> cars = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public CarService() {
        cars.add(new Car(idGen.getAndIncrement(), "Toyota"));
        cars.add(new Car(idGen.getAndIncrement(), "BMW"));
    }

    public List<Car> getAll() { return cars; }
    public Car getById(Long id) {
        return cars.stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }
    public Car add(String brand) {
        Car c = new Car(idGen.getAndIncrement(), brand);
        cars.add(c);
        return c;
    }
    public boolean delete(Long id) {
        return cars.removeIf(c -> c.getId().equals(id));
    }
    public List<Car> searchByBrand(String brand) {
        if (brand == null || brand.isEmpty()) return new ArrayList<>(cars);
        String lower = brand.toLowerCase();
        List<Car> result = new ArrayList<>();
        for (Car c : cars) {
            if (c.getBrand() != null && c.getBrand().toLowerCase().contains(lower)) {
                result.add(c);
            }
        }
        return result;
    }
    public List<Car> getPage(int page, int size) {
        if (size <= 0) return new ArrayList<>();
        int from = Math.max(0, page * size);
        int to = Math.min(cars.size(), from + size);
        if (from >= cars.size()) return new ArrayList<>();
        return cars.subList(from, to);
    }
} 