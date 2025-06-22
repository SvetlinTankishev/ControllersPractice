package org.example.service;

import org.example.models.entity.Animal;
import org.example.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    public List<Animal> getAll() {
        return animalRepository.findAll();
    }

    public Animal getById(Long id) {
        return animalRepository.findById(id).orElse(null);
    }

    public Animal add(String type) {
        Animal animal = new Animal();
        animal.setType(type);
        return animalRepository.save(animal);
    }

    public boolean delete(Long id) {
        if (animalRepository.existsById(id)) {
            animalRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Animal> searchByType(String type) {
        if (type == null || type.isEmpty()) return animalRepository.findAll();
        return animalRepository.findByTypeContainingIgnoreCase(type);
    }

    public List<Animal> getPage(int page, int size) {
        if (size <= 0) return List.of();
        return animalRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size)).getContent();
    }

    public Animal update(Long id, String type) {
        Animal animal = getById(id);
        if (animal == null) {
            return null;
        }
        animal.setType(type);
        return animalRepository.save(animal);
    }
} 