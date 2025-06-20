package org.example.action.unit;

import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AnimalServiceTest {
    private AnimalService service;

    @BeforeEach
    void setUp() {
        service = new AnimalService();
    }

    @Test
    void testGetAll() {
        List<Animal> animals = service.getAll();
        assertEquals(2, animals.size());
    }

    @Test
    void testGetById_found() {
        Animal animal = service.getById(1L);
        assertNotNull(animal);
        assertEquals("cat", animal.getType());
    }

    @Test
    void testGetById_notFound() {
        Animal animal = service.getById(999L);
        assertNull(animal);
    }

    @Test
    void testAdd() {
        Animal animal = service.add("parrot");
        assertNotNull(animal);
        assertEquals("parrot", animal.getType());
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