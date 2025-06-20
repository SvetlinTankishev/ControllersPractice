package org.example.rest.controller;

import org.example.entity.GovEmployee;
import org.example.dto.GovEmployeeDto;
import org.example.service.GovEmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/gov-employees")
public class GovEmployeeRestController {
    private final GovEmployeeService govEmployeeService = new GovEmployeeService();

    @GetMapping
    public List<GovEmployee> getAllEmployees() {
        return govEmployeeService.getAll();
    }

    @GetMapping("/{id}")
    public GovEmployee getEmployee(@PathVariable Long id) {
        GovEmployee emp = govEmployeeService.getById(id);
        if (emp == null) throw new NoSuchElementException("Employee not found");
        return emp;
    }

    @PostMapping
    public GovEmployee addEmployee(@RequestBody GovEmployeeDto dto) {
        return govEmployeeService.add(dto.getName());
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        govEmployeeService.delete(id);
    }
} 