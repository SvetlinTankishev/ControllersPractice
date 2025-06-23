package org.example.rest.unit;

import org.example.models.entity.Car;
import org.example.repository.CarRepository;
import org.example.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceUTest {
    @Mock
    private CarRepository carRepository;
    
    @InjectMocks
    private CarService carService;

    @Test
    void getAll_shouldReturnAllCars_whenRepositoryReturnsData() {
        // Given
        List<Car> expectedCars = Arrays.asList(
            new Car(1L, "Toyota"), 
            new Car(2L, "BMW")
        );
        when(carRepository.findAll()).thenReturn(expectedCars);
        
        // When
        List<Car> actualCars = carService.getAll();
        
        // Then
        assertThat(actualCars).hasSize(2);
        assertThat(actualCars).containsExactlyElementsOf(expectedCars);
        verify(carRepository).findAll();
    }

    @ParameterizedTest(name = "Get by ID: {0}")
    @MethodSource("getByIdData")
    void getById_shouldHandleVariousScenarios(String testName, Long carId, Optional<Car> repositoryResult, boolean shouldBePresent, String expectedBrand) {
        // Given
        when(carRepository.findById(carId)).thenReturn(repositoryResult);
        
        // When
        Optional<Car> result = carService.getById(carId);
        
        // Then
        assertThat(result.isPresent()).isEqualTo(shouldBePresent);
        if (shouldBePresent) {
            assertThat(result.get().getBrand()).isEqualTo(expectedBrand);
        }
        verify(carRepository).findById(carId);
    }

    static Stream<Arguments> getByIdData() {
        return Stream.of(
                Arguments.of("Car exists", 1L, Optional.of(new Car(1L, "Toyota")), true, "Toyota"),
                Arguments.of("Car does not exist", 999L, Optional.empty(), false, null)
        );
    }

    @Test
    void add_shouldReturnSavedCar_whenValidBrandProvided() {
        // Given
        String brand = "Honda";
        Car savedCar = new Car(3L, brand);
        when(carRepository.save(any(Car.class))).thenReturn(savedCar);
        
        // When
        Car result = carService.add(brand);
        
        // Then
        assertThat(result.getBrand()).isEqualTo(brand);
        assertThat(result.getId()).isEqualTo(3L);
        verify(carRepository).save(any(Car.class));
    }

    @ParameterizedTest(name = "Delete car: {0}")
    @MethodSource("deleteData")
    void delete_shouldHandleVariousScenarios(String testName, Long carId, boolean existsInRepo, boolean expectedResult, boolean shouldCallDelete) {
        // Given
        when(carRepository.existsById(carId)).thenReturn(existsInRepo);
        if (shouldCallDelete) {
            doNothing().when(carRepository).deleteById(carId);
        }
        
        // When
        boolean result = carService.delete(carId);
        
        // Then
        assertThat(result).isEqualTo(expectedResult);
        verify(carRepository).existsById(carId);
        if (shouldCallDelete) {
            verify(carRepository).deleteById(carId);
        } else {
            verify(carRepository, never()).deleteById(carId);
        }
    }

    static Stream<Arguments> deleteData() {
        return Stream.of(
                Arguments.of("Car exists", 1L, true, true, true),
                Arguments.of("Car does not exist", 999L, false, false, false)
        );
    }

    @ParameterizedTest(name = "Update car: {0}")
    @MethodSource("updateData")
    void update_shouldHandleVariousScenarios(String testName, Long carId, Optional<Car> repositoryResult, String newBrand, boolean shouldBePresent, boolean shouldCallSave) {
        // Given
        when(carRepository.findById(carId)).thenReturn(repositoryResult);
        if (shouldCallSave && repositoryResult.isPresent()) {
            Car updatedCar = new Car(carId, newBrand);
            when(carRepository.save(repositoryResult.get())).thenReturn(updatedCar);
        }
        
        // When
        Optional<Car> result = carService.update(carId, newBrand);
        
        // Then
        assertThat(result.isPresent()).isEqualTo(shouldBePresent);
        if (shouldBePresent) {
            assertThat(result.get().getBrand()).isEqualTo(newBrand);
        }
        verify(carRepository).findById(carId);
        if (shouldCallSave) {
            verify(carRepository).save(any(Car.class));
        } else {
            verify(carRepository, never()).save(any(Car.class));
        }
    }

    static Stream<Arguments> updateData() {
        return Stream.of(
                Arguments.of("Car exists", 1L, Optional.of(new Car(1L, "Old Brand")), "Updated Brand", true, true),
                Arguments.of("Car does not exist", 999L, Optional.empty(), "Updated Brand", false, false)
        );
    }

    @ParameterizedTest(name = "Search by brand: {0}")
    @MethodSource("searchByBrandData")
    void searchByBrand_shouldHandleVariousScenarios(String testName, String searchBrand, List<Car> expectedCars, boolean shouldCallSpecificSearch) {
        // Given
        if (shouldCallSpecificSearch) {
            when(carRepository.findByBrandContainingIgnoreCase(searchBrand)).thenReturn(expectedCars);
        } else {
            when(carRepository.findAll()).thenReturn(expectedCars);
        }
        
        // When
        List<Car> result = carService.searchByBrand(searchBrand);
        
        // Then
        assertThat(result).hasSize(expectedCars.size());
        assertThat(result).containsExactlyElementsOf(expectedCars);
        
        if (shouldCallSpecificSearch) {
            verify(carRepository).findByBrandContainingIgnoreCase(searchBrand);
            verify(carRepository, never()).findAll();
        } else {
            verify(carRepository).findAll();
            verify(carRepository, never()).findByBrandContainingIgnoreCase(any());
        }
    }

    static Stream<Arguments> searchByBrandData() {
        List<Car> bmwCars = Arrays.asList(new Car(1L, "BMW X5"));
        List<Car> allCars = Arrays.asList(new Car(1L, "BMW"), new Car(2L, "Toyota"));
        List<Car> emptyCars = Arrays.asList(); // No cars match "   "
        
        return Stream.of(
                Arguments.of("Specific brand provided", "BMW", bmwCars, true),
                Arguments.of("Brand is null", null, allCars, false),
                Arguments.of("Brand is empty", "", allCars, false),
                Arguments.of("Brand is blank spaces", "   ", emptyCars, true)
        );
    }

    @Test
    void getPage_shouldReturnPagedResults_whenValidParametersProvided() {
        // Given
        int page = 0;
        int size = 5;
        List<Car> expectedCars = Arrays.asList(new Car(1L, "BMW"), new Car(2L, "Toyota"));
        org.springframework.data.domain.PageRequest pageRequest = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Car> mockPage = mock(org.springframework.data.domain.Page.class);
        when(carRepository.findAll(pageRequest)).thenReturn(mockPage);
        when(mockPage.getContent()).thenReturn(expectedCars);
        
        // When
        List<Car> result = carService.getPage(page, size);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedCars);
        verify(carRepository).findAll(pageRequest);
        verify(mockPage).getContent();
    }
} 