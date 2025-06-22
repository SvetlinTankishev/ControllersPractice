package org.example.repository;

import org.example.models.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByBrandContainingIgnoreCase(String brand);
} 