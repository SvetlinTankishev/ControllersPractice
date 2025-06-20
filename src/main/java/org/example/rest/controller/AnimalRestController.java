package org.example.rest.controller;

import org.example.rest.entity.Animal;
import org.example.rest.dto.AnimalDto;
import org.example.rest.service.AnimalService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/animals")
public class AnimalRestController {
    private final AnimalService animalService = new AnimalService();

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAll();
    }

    @GetMapping("/{id}")
    public Animal getAnimal(@PathVariable Long id) {
        Animal animal = animalService.getById(id);
        if (animal == null) throw new NoSuchElementException("Animal not found");
        return animal;
    }

    @PostMapping
    public Animal addAnimal(@RequestBody AnimalDto dto) {
        return animalService.add(dto.getType());
    }

    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable Long id) {
        animalService.delete(id);
    }
} 