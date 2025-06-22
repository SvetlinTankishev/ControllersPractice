package org.example.service;

import org.example.models.entity.GovEmployee;
import org.example.repository.GovEmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GovEmployeeService {
    private final GovEmployeeRepository govEmployeeRepository;

    public GovEmployeeService(GovEmployeeRepository govEmployeeRepository) {
        this.govEmployeeRepository = govEmployeeRepository;
    }

    public List<GovEmployee> getAll() {
        return govEmployeeRepository.findAll();
    }

    public GovEmployee getById(Long id) {
        return govEmployeeRepository.findById(id).orElse(null);
    }

    public GovEmployee add(String name) {
        GovEmployee emp = new GovEmployee();
        emp.setName(name);
        return govEmployeeRepository.save(emp);
    }

    public boolean delete(Long id) {
        if (govEmployeeRepository.existsById(id)) {
            govEmployeeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<GovEmployee> searchByName(String name) {
        if (name == null || name.isEmpty()) return govEmployeeRepository.findAll();
        return govEmployeeRepository.findByNameContainingIgnoreCase(name);
    }

    public List<GovEmployee> getPage(int page, int size) {
        if (size <= 0) return List.of();
        return govEmployeeRepository.findAll(org.springframework.data.domain.PageRequest.of(page, size)).getContent();
    }

    public GovEmployee update(Long id, String name) {
        GovEmployee emp = getById(id);
        if (emp == null) {
            return null;
        }
        emp.setName(name);
        return govEmployeeRepository.save(emp);
    }
} 