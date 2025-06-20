package org.example.rest.controller;

import org.example.models.entity.GovEmployee;
import org.example.models.dto.GovEmployeeDto;
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

    @PatchMapping("/{id}")
    public GovEmployee patchEmployee(@PathVariable Long id, @RequestBody GovEmployeeDto dto) {
        GovEmployee emp = govEmployeeService.getById(id);
        if (emp == null) throw new NoSuchElementException("Employee not found");
        if (dto.getName() != null) {
            emp.setName(dto.getName());
        }
        // In a real app, persist the change here
        return emp;
    }

    @GetMapping("/search")
    public List<GovEmployee> searchEmployees(@RequestParam(required = false) String name) {
        return govEmployeeService.searchByName(name);
    }

    @GetMapping("/page")
    public List<GovEmployee> getEmployeesPage(@RequestParam int page, @RequestParam int size) {
        return govEmployeeService.getPage(page, size);
    }
} 