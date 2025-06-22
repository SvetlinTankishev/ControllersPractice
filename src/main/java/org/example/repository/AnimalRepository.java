package org.example.repository;

import org.example.models.entity.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {
    List<Animal> findByTypeContainingIgnoreCase(String type);
} 