package org.example.action.controller;

import org.example.action.entity.Animal;
import org.example.action.dto.AnimalDto;
import org.example.action.service.AnimalService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/action/animal")
public class AnimalActionController {
    private final AnimalService animalService = new AnimalService();

    @GetMapping("/list")
    public List<Animal> listAnimals() {
        return animalService.getAll();
    }

    @GetMapping("/get")
    public Animal getAnimal(@RequestParam Long id) {
        Animal animal = animalService.getById(id);
        if (animal == null) throw new NoSuchElementException("Animal not found");
        return animal;
    }

    @PostMapping("/add")
    public Animal addAnimal(@RequestBody AnimalDto dto) {
        return animalService.add(dto.getType());
    }

    @PostMapping("/remove")
    public void removeAnimal(@RequestParam Long id) {
        animalService.delete(id);
    }
} 