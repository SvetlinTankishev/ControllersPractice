package org.example.service;

import org.example.models.entity.Animal;
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
    public List<Animal> searchByType(String type) {
        if (type == null || type.isEmpty()) return new ArrayList<>(animals);
        String lower = type.toLowerCase();
        List<Animal> result = new ArrayList<>();
        for (Animal a : animals) {
            if (a.getType() != null && a.getType().toLowerCase().contains(lower)) {
                result.add(a);
            }
        }
        return result;
    }
    public List<Animal> getPage(int page, int size) {
        if (size <= 0) return new ArrayList<>();
        int from = Math.max(0, page * size);
        int to = Math.min(animals.size(), from + size);
        if (from >= animals.size()) return new ArrayList<>();
        return animals.subList(from, to);
    }
} 