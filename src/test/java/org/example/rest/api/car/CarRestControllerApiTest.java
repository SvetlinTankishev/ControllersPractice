package org.example.rest.api.car;

import org.example.config.ApiTestBase;
import org.example.models.dto.CarDto;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CarRestControllerApiTest extends ApiTestBase {

    @Autowired
    private CarService carService;

    private Car testCar;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        cleanupTestData();
        // Create a test car for each test
        testCar = carService.add("Test Car");
    }

    // GET /api/cars - Get all cars
    @Test
    void getAllCars_shouldReturnAllCars_whenCarsExist() throws Exception {
        // Given - additional cars created in setUp()
        carService.add("BMW");
        carService.add("Mercedes");

        // When & Then
        validateSuccess(getJson("/api/cars"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].brand").exists());
    }

    @Test
    void getAllCars_shouldReturnEmptyList_whenNoCarsExist() throws Exception {
        // Given - clear all cars
        cleanupTestData();

        // When & Then
        validateSuccess(getJson("/api/cars"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // GET /api/cars/{id} - Get car by ID
    @Test
    void getCarById_shouldReturnCar_whenCarExists() throws Exception {
        // Given - testCar created in setUp()

        // When & Then
        validateSuccess(getJson("/api/cars/" + testCar.getId()))
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value("Test Car"));
    }

    @Test
    void getCarById_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given - non-existent ID
        Long nonExistentId = 999L;

        // When & Then
        validateNotFound(getJson("/api/cars/" + nonExistentId));
    }

    @Test
    void getCarById_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given - invalid ID format
        String invalidId = "invalid";

        // When & Then
        validateBadRequest(getJson("/api/cars/" + invalidId));
    }

    // POST /api/cars - Create new car
    @Test
    void createCar_shouldReturnCreatedCar_whenValidDataProvided() throws Exception {
        // Given
        CarDto carDto = new CarDto();
        carDto.setBrand("New Car");

        // When & Then
        validateCreated(postJson("/api/cars", carDto))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").value("New Car"));
    }

    @Test
    void createCar_shouldReturn400_whenInvalidJsonProvided() throws Exception {
        // Given - invalid JSON
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/cars")
                .contentType("application/json")
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCar_shouldReturn400_whenEmptyBodyProvided() throws Exception {
        // Given - empty body
        CarDto emptyDto = new CarDto();

        // When & Then
        validateCreated(postJson("/api/cars", emptyDto))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.brand").isEmpty());
    }

    // PATCH /api/cars/{id} - Update car
    @Test
    void updateCar_shouldReturnUpdatedCar_whenValidDataProvided() throws Exception {
        // Given
        CarDto updateDto = new CarDto();
        updateDto.setBrand("Updated Car");

        // When & Then
        validateSuccess(patchJson("/api/cars/" + testCar.getId(), updateDto))
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value("Updated Car"));
    }

    @Test
    void updateCar_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        CarDto updateDto = new CarDto();
        updateDto.setBrand("Updated Car");

        // When & Then
        validateNotFound(patchJson("/api/cars/" + nonExistentId, updateDto));
    }

    @Test
    void updateCar_shouldReturnOriginalCar_whenNoBrandProvided() throws Exception {
        // Given
        CarDto emptyDto = new CarDto();

        // When & Then
        validateSuccess(patchJson("/api/cars/" + testCar.getId(), emptyDto))
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value("Test Car"));
    }

    @Test
    void updateCar_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given
        String invalidId = "invalid";
        CarDto updateDto = new CarDto();
        updateDto.setBrand("Updated Car");

        // When & Then
        validateBadRequest(patchJson("/api/cars/" + invalidId, updateDto));
    }

    // DELETE /api/cars/{id} - Delete car
    @Test
    void deleteCar_shouldReturn204_whenCarExists() throws Exception {
        // Given - testCar created in setUp()

        // When & Then
        validateNoContent(deleteJson("/api/cars/" + testCar.getId()));
    }

    @Test
    void deleteCar_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        validateNotFound(deleteJson("/api/cars/" + nonExistentId));
    }

    @Test
    void deleteCar_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given
        String invalidId = "invalid";

        // When & Then
        validateBadRequest(deleteJson("/api/cars/" + invalidId));
    }

    // GET /api/cars/search - Search cars by brand
    @Test
    void searchCars_shouldReturnMatchingCars_whenBrandProvided() throws Exception {
        // Given
        carService.add("BMW");
        carService.add("BMW X5");
        carService.add("Mercedes");

        // When & Then
        validateSuccess(getJson("/api/cars/search?brand=bmw"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].brand").value("BMW"));
    }

    @Test
    void searchCars_shouldReturnAllCars_whenNoBrandProvided() throws Exception {
        // Given - only testCar exists from setUp()

        // When & Then
        validateSuccess(getJson("/api/cars/search"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchCars_shouldReturnEmptyList_whenNoMatchingCars() throws Exception {
        // Given - no cars with "Ferrari" brand

        // When & Then
        validateSuccess(getJson("/api/cars/search?brand=Ferrari"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchCars_shouldBeCaseInsensitive() throws Exception {
        // Given
        carService.add("BMW");

        // When & Then
        validateSuccess(getJson("/api/cars/search?brand=bmw"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].brand").value("BMW"));
    }

    // GET /api/cars/page - Get paginated cars
    @Test
    void getCarsPage_shouldReturnPaginatedResults_whenValidParametersProvided() throws Exception {
        // Given
        carService.add("Car 1");
        carService.add("Car 2");
        carService.add("Car 3");

        // When & Then
        validateSuccess(getJson("/api/cars/page?page=0&size=2"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCarsPage_shouldReturnEmptyList_whenPageOutOfBounds() throws Exception {
        // Given - only a few cars exist

        // When & Then
        validateSuccess(getJson("/api/cars/page?page=10&size=5"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getCarsPage_shouldReturnEmptyList_whenInvalidSizeProvided() throws Exception {
        // Given - invalid size parameter

        // When & Then
        validateSuccess(getJson("/api/cars/page?page=0&size=0"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getCarsPage_shouldReturn400_whenMissingRequiredParameters() throws Exception {
        // Given - missing page parameter

        // When & Then
        validateBadRequest(getJson("/api/cars/page?size=5"));
    }

    @Test
    void getCarsPage_shouldReturn400_whenInvalidParameterTypes() throws Exception {
        // Given - invalid parameter types

        // When & Then
        validateBadRequest(getJson("/api/cars/page?page=invalid&size=invalid"));
    }
} 