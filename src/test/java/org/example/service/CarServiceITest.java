package org.example.service;

import org.example.models.entity.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.example.config.IntegrationTestBase;

@SpringBootTest
@Transactional
public class CarServiceITest extends IntegrationTestBase {
    @Autowired
    private CarService carService;

    @Test
    void addAndGetCar() {
        Car car = carService.add("Honda");
        assertThat(car.getId()).isNotNull();
        Car found = carService.getById(car.getId());
        assertThat(found).isNotNull();
        assertThat(found.getBrand()).isEqualTo("Honda");
    }

    @Test
    void deleteCar() {
        Car car = carService.add("Toyota");
        boolean deleted = carService.delete(car.getId());
        assertThat(deleted).isTrue();
        assertThat(carService.getById(car.getId())).isNull();
    }

    @Test
    void searchByBrand() {
        carService.add("BMW");
        List<Car> bmws = carService.searchByBrand("BMW");
        assertThat(bmws).anyMatch(c -> c.getBrand().equals("BMW"));
    }

    @Test
    void getPage() {
        carService.add("Toyota");
        carService.add("BMW");
        List<Car> page = carService.getPage(0, 2);
        assertThat(page.size()).isLessThanOrEqualTo(2);
    }
} 