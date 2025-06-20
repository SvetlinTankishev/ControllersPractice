package org.example.action.service;

import org.example.action.entity.Animal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class AnimalService {
    private final List<Animal> animals = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public AnimalService() {
        animals.add(new Animal(idGen.getAndIncrement(), "cat"));
        animals.add(new Animal(idGen.getAndIncrement(), "dog"));
    }

    public List<Animal> getAll() { return animals; }
    public Animal getById(Long id) {
        return animals.stream().filter(a -> a.getId().equals(id)).findFirst().orElse(null);
    }
    public Animal add(String type) {
        Animal a = new Animal(idGen.getAndIncrement(), type);
        animals.add(a);
        return a;
    }
    public boolean delete(Long id) {
        return animals.removeIf(a -> a.getId().equals(id));
    }
} 