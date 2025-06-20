package org.example.rest.controller;

import org.example.entity.Animal;
import org.example.dto.AnimalDto;
import org.example.service.AnimalService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

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

    @PatchMapping("/{id}")
    public Animal patchAnimal(@PathVariable Long id, @RequestBody AnimalDto dto) {
        Animal animal = animalService.getById(id);
        if (animal == null) throw new NoSuchElementException("Animal not found");
        if (dto.getType() != null) {
            animal.setType(dto.getType());
        }
        // In a real app, persist the change here
        return animal;
    }

    @GetMapping("/search")
    public List<Animal> searchAnimals(@RequestParam(required = false) String type) {
        return animalService.searchByType(type);
    }
} 