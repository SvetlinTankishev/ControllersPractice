package org.example.action.controller;

import org.example.models.entity.GovEmployee;
import org.example.models.dto.GovEmployeeDto;
import org.example.service.GovEmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/action/gov-employee")
public class GovEmployeeActionController {
    private final GovEmployeeService govEmployeeService = new GovEmployeeService();

    @GetMapping("/list")
    public List<GovEmployee> listEmployees() {
        return govEmployeeService.getAll();
    }

    @GetMapping("/get")
    public GovEmployee getEmployee(@RequestParam Long id) {
        GovEmployee emp = govEmployeeService.getById(id);
        if (emp == null) throw new NoSuchElementException("Employee not found");
        return emp;
    }

    @PostMapping("/add")
    public GovEmployee addEmployee(@RequestBody GovEmployeeDto dto) {
        return govEmployeeService.add(dto.getName());
    }

    @PostMapping("/remove")
    public void removeEmployee(@RequestParam Long id) {
        govEmployeeService.delete(id);
    }

    @PostMapping("/update")
    public GovEmployee updateEmployee(@RequestParam Long id, @RequestBody GovEmployeeDto dto) {
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