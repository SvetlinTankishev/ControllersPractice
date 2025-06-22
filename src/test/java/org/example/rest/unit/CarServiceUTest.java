package org.example.rest.unit;

import org.example.models.entity.Car;
import org.example.repository.CarRepository;
import org.example.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CarServiceUTest {
    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private CarService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(carRepository.findAll()).thenReturn(Arrays.asList(new Car(1L, "Toyota"), new Car(2L, "BMW")));
        List<Car> cars = service.getAll();
        assertThat(cars).hasSize(2);
    }

    @Test
    void testGetById_found() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(new Car(1L, "Toyota")));
        Car car = service.getById(1L);
        assertThat(car).isNotNull();
        assertThat(car.getBrand()).isEqualTo("Toyota");
    }

    @Test
    void testGetById_notFound() {
        when(carRepository.findById(999L)).thenReturn(Optional.empty());
        Car car = service.getById(999L);
        assertThat(car).isNull();
    }

    @Test
    void testAdd() {
        Car toSave = new Car(null, "Honda");
        Car saved = new Car(3L, "Honda");
        when(carRepository.save(any(Car.class))).thenReturn(saved);
        Car car = service.add("Honda");
        assertThat(car.getBrand()).isEqualTo("Honda");
        assertThat(car.getId()).isEqualTo(3L);
    }

    @Test
    void testDelete_existing() {
        when(carRepository.existsById(1L)).thenReturn(true);
        doNothing().when(carRepository).deleteById(1L);
        boolean removed = service.delete(1L);
        assertThat(removed).isTrue();
    }

    @Test
    void testDelete_nonExisting() {
        when(carRepository.existsById(999L)).thenReturn(false);
        boolean removed = service.delete(999L);
        assertThat(removed).isFalse();
    }
} 