package org.example.rest.controller;

import org.example.models.entity.GovEmployee;
import org.example.models.dto.GovEmployeeDto;
import org.example.service.GovEmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/gov-employees")
public class GovEmployeeRestController {
    private final GovEmployeeService govEmployeeService;

    public GovEmployeeRestController(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @GetMapping
    public List<GovEmployee> getAllEmployees() {
        return govEmployeeService.getAll();
    }

    @GetMapping("/{id}")
    public GovEmployee getEmployee(@PathVariable("id") Long id) {
        GovEmployee emp = govEmployeeService.getById(id);
        if (emp == null) throw new NoSuchElementException("Employee not found");
        return emp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GovEmployee addEmployee(@RequestBody GovEmployeeDto dto) {
        return govEmployeeService.add(dto.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        boolean deleted = govEmployeeService.delete(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public GovEmployee patchEmployee(@PathVariable("id") Long id, @RequestBody GovEmployeeDto dto) {
        if (dto.getName() != null) {
            GovEmployee updatedEmployee = govEmployeeService.update(id, dto.getName());
            if (updatedEmployee == null) {
                throw new NoSuchElementException("Employee not found");
            }
            return updatedEmployee;
        }
        return govEmployeeService.getById(id);
    }

    @GetMapping("/search")
    public List<GovEmployee> searchEmployees(@RequestParam(value = "name", required = false) String name) {
        return govEmployeeService.searchByName(name);
    }

    @GetMapping("/page")
    public List<GovEmployee> getEmployeesPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        return govEmployeeService.getPage(page, size);
    }
} 