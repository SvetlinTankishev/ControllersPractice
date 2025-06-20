package org.example.service;

import org.example.entity.Car;
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
} 