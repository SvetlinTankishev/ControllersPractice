package org.example.repository;

import org.example.config.IntegrationTestBase;
import org.example.models.entity.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class CarRepositoryITest extends IntegrationTestBase {
    @Autowired
    private CarRepository carRepository;

    @Test
    void saveAndFindCar() {
        Car car = new Car();
        car.setBrand("Toyota");
        Car saved = carRepository.save(car);
        assertThat(saved.getId()).isNotNull();
        Car found = carRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getBrand()).isEqualTo("Toyota");
    }

    @Test
    void findAllAndDelete() {
        Car c1 = carRepository.save(new Car(null, "Toyota"));
        Car c2 = carRepository.save(new Car(null, "BMW"));
        List<Car> all = carRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        carRepository.delete(c1);
        assertThat(carRepository.findById(c1.getId())).isEmpty();
    }
} 