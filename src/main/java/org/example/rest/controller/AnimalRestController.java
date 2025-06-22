package org.example.rest.controller;

import org.example.models.entity.Animal;
import org.example.models.dto.AnimalDto;
import org.example.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/animals")
public class AnimalRestController {
    private final AnimalService animalService;

    public AnimalRestController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public List<Animal> getAllAnimals() {
        return animalService.getAll();
    }

    @GetMapping("/{id}")
    public Animal getAnimal(@PathVariable("id") Long id) {
        Animal animal = animalService.getById(id);
        if (animal == null) throw new NoSuchElementException("Animal not found");
        return animal;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Animal addAnimal(@RequestBody AnimalDto dto) {
        return animalService.add(dto.getType());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable("id") Long id) {
        boolean deleted = animalService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public Animal patchAnimal(@PathVariable("id") Long id, @RequestBody AnimalDto dto) {
        if (dto.getType() != null) {
            Animal updatedAnimal = animalService.update(id, dto.getType());
            if (updatedAnimal == null) {
                throw new NoSuchElementException("Animal not found");
            }
            return updatedAnimal;
        }
        return animalService.getById(id);
    }

    @GetMapping("/search")
    public List<Animal> searchAnimals(@RequestParam(value = "type", required = false) String type) {
        return animalService.searchByType(type);
    }

    @GetMapping("/page")
    public List<Animal> getAnimalsPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        return animalService.getPage(page, size);
    }
} 