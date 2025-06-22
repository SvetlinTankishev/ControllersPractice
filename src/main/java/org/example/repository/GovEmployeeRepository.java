package org.example.repository;

import org.example.models.entity.GovEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovEmployeeRepository extends JpaRepository<GovEmployee, Long> {
    List<GovEmployee> findByNameContainingIgnoreCase(String name);
} 