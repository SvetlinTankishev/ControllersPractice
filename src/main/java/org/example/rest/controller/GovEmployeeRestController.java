package org.example.rest.controller;

import org.example.models.entity.GovEmployee;
import org.example.models.dto.GovEmployeeDto;
import org.example.service.GovEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
public class GovEmployeeRestController {
    private final GovEmployeeService govEmployeeService;

    public GovEmployeeRestController(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @GetMapping
    public ResponseEntity<List<GovEmployee>> getAllEmployees() {
        return ResponseEntity.ok(govEmployeeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GovEmployee> getEmployee(@PathVariable("id") Long id) {
        Optional<GovEmployee> employee = govEmployeeService.getById(id);
        return employee.map(ResponseEntity::ok)
                      .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GovEmployee> addEmployee(@Valid @RequestBody GovEmployeeDto dto) {
        GovEmployee createdEmployee = govEmployeeService.add(dto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        boolean deleted = govEmployeeService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new NoSuchElementException("Employee not found with id: " + id);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GovEmployee> patchEmployee(@PathVariable("id") Long id, @Valid @RequestBody GovEmployeeDto dto) {
        if (dto.getName() != null) {
            Optional<GovEmployee> updatedEmployee = govEmployeeService.update(id, dto.getName());
            return updatedEmployee.map(ResponseEntity::ok)
                                 .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
        }
        Optional<GovEmployee> employee = govEmployeeService.getById(id);
        return employee.map(ResponseEntity::ok)
                      .orElseThrow(() -> new NoSuchElementException("Employee not found with id: " + id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<GovEmployee>> searchEmployees(@RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(govEmployeeService.searchByName(name));
    }

    @GetMapping("/page")
    public ResponseEntity<List<GovEmployee>> getEmployeesPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Page must be >= 0 and size must be > 0");
        }
        return ResponseEntity.ok(govEmployeeService.getPage(page, size));
    }
} 