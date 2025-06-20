package org.example.action.controller;

import org.example.action.entity.GovEmployee;
import org.example.action.dto.GovEmployeeDto;
import org.example.action.service.GovEmployeeService;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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
} 