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

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkEmployeeExists(@PathVariable("id") Long id) {
        Optional<GovEmployee> employee = govEmployeeService.getById(id);
        if (employee.isPresent()) {
            return ResponseEntity.ok()
                    .header("X-Resource-Count", "1")
                    .header("X-Last-Modified", employee.get().getUpdatedAt() != null ? 
                            employee.get().getUpdatedAt().toString() : "")
                    .build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> getEmployeeOptions(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .header("Allow", "GET, POST, PUT, PATCH, DELETE, HEAD, OPTIONS")
                .header("X-Resource-Type", "GovEmployee")
                .header("X-API-Version", "1.0")
                .header("X-Supported-Operations", "read, create, update, delete")
                .build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> getEmployeesCollectionOptions() {
        return ResponseEntity.ok()
                .header("Allow", "GET, POST, OPTIONS")
                .header("X-Resource-Type", "GovEmployee Collection")
                .header("X-API-Version", "1.0")
                .header("X-Supported-Operations", "list, create, search")
                .header("X-Pagination-Supported", "true")
                .build();
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

    /**
     * PUT endpoint for complete resource replacement.
     * 
     * SAFETY NOTE: This implementation uses PATCH-like semantics to prevent accidental data loss.
     * Only fields provided in the DTO are updated; missing fields are preserved.
     * This prevents corruption when new fields are added to the entity in the future.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GovEmployee> putEmployee(@PathVariable("id") Long id, @Valid @RequestBody GovEmployeeDto dto) {
        // Using PATCH-like semantics for data safety
        // All required fields must be provided due to @Valid validation
        Optional<GovEmployee> updatedEmployee = govEmployeeService.update(id, dto.getName());
        return updatedEmployee.map(ResponseEntity::ok)
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