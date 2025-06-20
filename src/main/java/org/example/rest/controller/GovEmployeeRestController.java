package org.example.rest.controller;

import org.example.rest.entity.GovEmployee;
import org.example.rest.dto.GovEmployeeDto;
import org.example.rest.service.GovEmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/gov-employees")
public class GovEmployeeRestController {
    private final org.example.rest.service.GovEmployeeService govEmployeeService = new org.example.rest.service.GovEmployeeService();

    @GetMapping
    public List<org.example.rest.entity.GovEmployee> getAllEmployees() {
        return govEmployeeService.getAll();
    }

    @GetMapping("/{id}")
    public org.example.rest.entity.GovEmployee getEmployee(@PathVariable Long id) {
        org.example.rest.entity.GovEmployee emp = govEmployeeService.getById(id);
        if (emp == null) throw new NoSuchElementException("Employee not found");
        return emp;
    }

    @PostMapping
    public org.example.rest.entity.GovEmployee addEmployee(@RequestBody org.example.rest.dto.GovEmployeeDto dto) {
        return govEmployeeService.add(dto.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        govEmployeeService.delete(id);
    }
} 