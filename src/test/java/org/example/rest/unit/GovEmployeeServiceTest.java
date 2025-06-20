package org.example.rest.unit;

import org.example.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GovEmployeeServiceTest {
    private GovEmployeeService service;

    @BeforeEach
    void setUp() {
        service = new GovEmployeeService();
    }

    @Test
    void testGetAll() {
        List<GovEmployee> employees = service.getAll();
        assertEquals(2, employees.size());
    }

    @Test
    void testGetById_found() {
        GovEmployee emp = service.getById(1L);
        assertNotNull(emp);
        assertEquals("Alice", emp.getName());
    }

    @Test
    void testGetById_notFound() {
        GovEmployee emp = service.getById(999L);
        assertNull(emp);
    }

    @Test
    void testAdd() {
        GovEmployee emp = service.add("Charlie");
        assertNotNull(emp);
        assertEquals("Charlie", emp.getName());
        assertEquals(3, service.getAll().size());
    }

    @Test
    void testDelete_existing() {
        boolean removed = service.delete(1L);
        assertTrue(removed);
        assertEquals(1, service.getAll().size());
    }

    @Test
    void testDelete_nonExisting() {
        boolean removed = service.delete(999L);
        assertFalse(removed);
    }
} 