package org.example.service;

import org.example.models.entity.GovEmployee;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class GovEmployeeService {
    private final List<GovEmployee> employees = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public GovEmployeeService() {
        employees.add(new GovEmployee(idGen.getAndIncrement(), "Alice"));
        employees.add(new GovEmployee(idGen.getAndIncrement(), "Bob"));
    }

    public List<GovEmployee> getAll() { return employees; }
    public GovEmployee getById(Long id) {
        return employees.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
    }
    public GovEmployee add(String name) {
        GovEmployee e = new GovEmployee(idGen.getAndIncrement(), name);
        employees.add(e);
        return e;
    }
    public boolean delete(Long id) {
        return employees.removeIf(e -> e.getId().equals(id));
    }
    public List<GovEmployee> searchByName(String name) {
        if (name == null || name.isEmpty()) return new ArrayList<>(employees);
        String lower = name.toLowerCase();
        List<GovEmployee> result = new ArrayList<>();
        for (GovEmployee e : employees) {
            if (e.getName() != null && e.getName().toLowerCase().contains(lower)) {
                result.add(e);
            }
        }
        return result;
    }
    public List<GovEmployee> getPage(int page, int size) {
        if (size <= 0) return new ArrayList<>();
        int from = Math.max(0, page * size);
        int to = Math.min(employees.size(), from + size);
        if (from >= employees.size()) return new ArrayList<>();
        return employees.subList(from, to);
    }
} 