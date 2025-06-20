package org.example.rest.unit;

import org.example.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest {
    private CarService service;

    @BeforeEach
    void setUp() {
        service = new CarService();
    }

    @Test
    void testGetAll() {
        List<Car> cars = service.getAll();
        assertEquals(2, cars.size());
    }

    @Test
    void testGetById_found() {
        Car car = service.getById(1L);
        assertNotNull(car);
        assertEquals("Toyota", car.getBrand());
    }

    @Test
    void testGetById_notFound() {
        Car car = service.getById(999L);
        assertNull(car);
    }

    @Test
    void testAdd() {
        Car car = service.add("Honda");
        assertNotNull(car);
        assertEquals("Honda", car.getBrand());
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