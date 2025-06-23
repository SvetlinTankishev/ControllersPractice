package org.example.service;

import org.example.models.entity.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.example.config.IntegrationTestBase;

@SpringBootTest
@Transactional
public class CarServiceITest extends IntegrationTestBase {
    @Autowired
    private CarService carService;

    @Test
    void addAndGetCar_shouldCreateAndRetrieveCar_whenValidDataProvided() {
        // Given
        String brand = "Honda";
        
        // When
        Car savedCar = carService.add(brand);
        Optional<Car> retrievedCar = carService.getById(savedCar.getId());
        
        // Then
        assertThat(savedCar.getId()).isNotNull();
        assertThat(savedCar.getBrand()).isEqualTo(brand);
        assertThat(retrievedCar).isPresent();
        assertThat(retrievedCar.get().getBrand()).isEqualTo(brand);
        assertThat(retrievedCar.get().getCreatedAt()).isNotNull();
        assertThat(retrievedCar.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void deleteCar_shouldRemoveCar_whenCarExists() {
        // Given
        Car savedCar = carService.add("Toyota");
        
        // When
        boolean deleted = carService.delete(savedCar.getId());
        Optional<Car> retrievedCar = carService.getById(savedCar.getId());
        
        // Then
        assertThat(deleted).isTrue();
        assertThat(retrievedCar).isEmpty();
    }

    @Test
    void deleteCar_shouldReturnFalse_whenCarDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        
        // When
        boolean deleted = carService.delete(nonExistentId);
        
        // Then
        assertThat(deleted).isFalse();
    }

    @Test
    void searchByBrand_shouldReturnMatchingCars_whenBrandExists() {
        // Given
        carService.add("BMW");
        carService.add("BMW X5");
        carService.add("Toyota");
        
        // When
        List<Car> bmwCars = carService.searchByBrand("BMW");
        
        // Then
        assertThat(bmwCars).hasSize(2);
        assertThat(bmwCars).allMatch(car -> car.getBrand().contains("BMW"));
    }

    @Test
    void updateCar_shouldModifyBrand_whenCarExists() {
        // Given
        Car originalCar = carService.add("Toyota");
        String newBrand = "Honda";
        
        // When
        Optional<Car> updatedCar = carService.update(originalCar.getId(), newBrand);
        
        // Then
        assertThat(updatedCar).isPresent();
        assertThat(updatedCar.get().getBrand()).isEqualTo(newBrand);
        assertThat(updatedCar.get().getId()).isEqualTo(originalCar.getId());
    }

    @Test
    void updateCar_shouldReturnEmpty_whenCarDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        String newBrand = "Honda";
        
        // When
        Optional<Car> result = carService.update(nonExistentId, newBrand);
        
        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void getPage_shouldReturnPaginatedResults_whenValidParametersProvided() {
        // Given
        carService.add("Toyota");
        carService.add("BMW");
        carService.add("Honda");
        
        // When
        List<Car> firstPage = carService.getPage(0, 2);
        
        // Then
        assertThat(firstPage).hasSizeLessThanOrEqualTo(2);
    }

    @Test
    void getPage_shouldReturnEmptyList_whenInvalidSizeProvided() {
        // Given
        carService.add("Toyota");
        
        // When
        List<Car> result = carService.getPage(0, 0);
        
        // Then
        assertThat(result).isEmpty();
    }
} 