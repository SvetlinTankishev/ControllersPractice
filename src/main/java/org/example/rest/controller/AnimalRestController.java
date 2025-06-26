package org.example.rest.controller;

import org.example.models.entity.Animal;
import org.example.models.dto.AnimalDto;
import org.example.service.AnimalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/animals")
public class AnimalRestController {
    private final AnimalService animalService;

    public AnimalRestController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable("id") Long id) {
        Optional<Animal> animal = animalService.getById(id);
        return animal.map(ResponseEntity::ok)
                     .orElseThrow(() -> new NoSuchElementException("Animal not found with id: " + id));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkAnimalExists(@PathVariable("id") Long id) {
        Optional<Animal> animal = animalService.getById(id);
        if (animal.isPresent()) {
            return ResponseEntity.ok()
                    .header("X-Resource-Count", "1")
                    .header("X-Last-Modified", animal.get().getUpdatedAt() != null ? 
                            animal.get().getUpdatedAt().toString() : "")
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> getAnimalOptions(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .header("Allow", "GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS")
                .header("X-Resource-Type", "Animal")
                .header("X-API-Version", "1.0")
                .header("X-Supported-Operations", "read, create, update, delete")
                .build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> getAnimalsCollectionOptions() {
        return ResponseEntity.ok()
                .header("Allow", "GET, POST, OPTIONS")
                .header("X-Resource-Type", "Animal Collection")
                .header("X-API-Version", "1.0")
                .header("X-Supported-Operations", "list, create, search")
                .header("X-Pagination-Supported", "true")
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Animal> addAnimal(@Valid @RequestBody AnimalDto dto) {
        Animal createdAnimal = animalService.add(dto.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAnimal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnimal(@PathVariable("id") Long id) {
        boolean deleted = animalService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NoSuchElementException("Animal not found with id: " + id);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Animal> patchAnimal(@PathVariable("id") Long id, @Valid @RequestBody AnimalDto dto) {
        if (dto.getType() != null) {
            Optional<Animal> updatedAnimal = animalService.update(id, dto.getType());
            return updatedAnimal.map(ResponseEntity::ok)
                               .orElseThrow(() -> new NoSuchElementException("Animal not found with id: " + id));
        }
        Optional<Animal> animal = animalService.getById(id);
        return animal.map(ResponseEntity::ok)
                     .orElseThrow(() -> new NoSuchElementException("Animal not found with id: " + id));
    }

    /**
     * PUT endpoint for complete resource replacement.
     * 
     * SAFETY NOTE: This implementation uses PATCH-like semantics to prevent accidental data loss.
     * Only fields provided in the DTO are updated; missing fields are preserved.
     * This prevents corruption when new fields are added to the entity in the future.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Animal> putAnimal(@PathVariable("id") Long id, @Valid @RequestBody AnimalDto dto) {
        // Using PATCH-like semantics for data safety
        // All required fields must be provided due to @Valid validation
        Optional<Animal> updatedAnimal = animalService.update(id, dto.getType());
        return updatedAnimal.map(ResponseEntity::ok)
                           .orElseThrow(() -> new NoSuchElementException("Animal not found with id: " + id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Animal>> searchAnimals(@RequestParam(value = "type", required = false) String type) {
        return ResponseEntity.ok(animalService.searchByType(type));
    }

    @GetMapping("/page")
    public ResponseEntity<List<Animal>> getAnimalsPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
        }
        return ResponseEntity.ok(animalService.getPage(page, size));
    }
} 