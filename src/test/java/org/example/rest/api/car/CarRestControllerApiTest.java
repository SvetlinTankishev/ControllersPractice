package org.example.rest.api.car;

import org.example.config.ApiTestBase;
import org.example.models.dto.CarDto;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

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
        validateBadRequest(getJson("/api/cars/" + invalidId))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid JSON format"));
    }

    // Parameterized test for validation scenarios
    @ParameterizedTest(name = "Create car validation: {0}")
    @MethodSource("invalidCarCreationData")
    void createCar_shouldReturn400_whenInvalidDataProvided(String testName, String brand, String expectedError) throws Exception {
        // Given
        CarDto invalidDto = new CarDto();
        if (brand != null) {
            invalidDto.setBrand(brand);
        }

        // When & Then
        validateBadRequest(postJson("/api/cars", invalidDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value(expectedError));
    }

    static Stream<Arguments> invalidCarCreationData() {
        return Stream.of(
                Arguments.of("Empty brand", null, "brand: Brand cannot be blank"),
                Arguments.of("Brand too short", "A", "brand: Brand must be between 2 and 50 characters"),
                Arguments.of("Brand too long", "A".repeat(51), "brand: Brand must be between 2 and 50 characters"),
                Arguments.of("Brand with spaces only", "   ", "brand: Brand cannot be blank")
        );
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
    void updateCar_shouldReturn400_whenNoBrandProvided() throws Exception {
        // Given - empty DTO should fail validation
        CarDto emptyDto = new CarDto();

        // When & Then
        validateBadRequest(patchJson("/api/cars/" + testCar.getId(), emptyDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value("brand: Brand cannot be blank"));
    }

    // Parameterized test for invalid ID scenarios
    @ParameterizedTest(name = "Invalid ID test: {0}")
    @ValueSource(strings = {"update", "replace", "delete"})
    void shouldReturn400_whenInvalidIdProvided(String operation) throws Exception {
        // Given
        String invalidId = "invalid";

        // When & Then
        switch (operation) {
            case "update":
                CarDto updateDto = new CarDto();
                updateDto.setBrand("Updated Car");
                validateBadRequest(patchJson("/api/cars/" + invalidId, updateDto))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
            case "replace":
                CarDto replaceDto = new CarDto();
                replaceDto.setBrand("Replaced Car");
                validateBadRequest(putJson("/api/cars/" + invalidId, replaceDto))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
            case "delete":
                validateBadRequest(deleteJson("/api/cars/" + invalidId))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
        }
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

    // Parameterized test for pagination error scenarios
    @ParameterizedTest(name = "Pagination error: {0}")
    @MethodSource("invalidPaginationData")
    void getCarsPage_shouldReturn400_whenInvalidParametersProvided(String testName, String url, String expectedErrorCode, String expectedMessage) throws Exception {
        // When & Then
        var result = validateBadRequest(getJson(url))
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));
        
        if (expectedMessage != null) {
            result.andExpect(jsonPath("$.message").value(expectedMessage));
        }
    }

    static Stream<Arguments> invalidPaginationData() {
        return Stream.of(
                Arguments.of("Invalid size (0)", "/api/cars/page?page=0&size=0", "BAD_REQUEST", "Page must be >= 0 and size must be > 0"),
                Arguments.of("Missing page parameter", "/api/cars/page?size=5", "VALIDATION_ERROR", "Input validation failed"),
                Arguments.of("Invalid parameter types", "/api/cars/page?page=invalid&size=invalid", "BAD_REQUEST", null)
        );
    }

    // PUT /api/cars/{id} - Replace car (with data safety)
    @Test
    void replaceCar_shouldReturnUpdatedCar_whenValidDataProvided() throws Exception {
        // Given
        CarDto replaceDto = new CarDto();
        replaceDto.setBrand("Replaced Car");

        // When & Then
        validateSuccess(putJson("/api/cars/" + testCar.getId(), replaceDto))
                .andExpect(jsonPath("$.id").value(testCar.getId()))
                .andExpect(jsonPath("$.brand").value("Replaced Car"));
    }

    @Test
    void replaceCar_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        CarDto replaceDto = new CarDto();
        replaceDto.setBrand("Replaced Car");

        // When & Then
        validateNotFound(putJson("/api/cars/" + nonExistentId, replaceDto));
    }

    @Test
    void replaceCar_shouldReturn400_whenNoBrandProvided() throws Exception {
        // Given - empty DTO should fail validation to prevent data corruption
        CarDto emptyDto = new CarDto();

        // When & Then
        validateBadRequest(putJson("/api/cars/" + testCar.getId(), emptyDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value("brand: Brand cannot be blank"));
    }

    @Test
    void replaceCar_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given
        String invalidId = "invalid";
        CarDto replaceDto = new CarDto();
        replaceDto.setBrand("Replaced Car");

        // When & Then
        validateBadRequest(putJson("/api/cars/" + invalidId, replaceDto))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
    }

    @ParameterizedTest(name = "PUT car validation: {0}")
    @MethodSource("invalidCarCreationData")
    void replaceCar_shouldReturn400_whenInvalidDataProvided(String testName, String brand, String expectedError) throws Exception {
        // Given
        CarDto invalidDto = new CarDto();
        if (brand != null) {
            invalidDto.setBrand(brand);
        }

        // When & Then
        validateBadRequest(putJson("/api/cars/" + testCar.getId(), invalidDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value(expectedError));
    }
} 