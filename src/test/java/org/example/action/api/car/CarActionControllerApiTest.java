package org.example.action.api.car;

import org.example.action.car.request.GetCarByIdRequest;
import org.example.action.car.request.UpdateCarRequest;
import org.example.action.car.request.DeleteCarRequest;
import org.example.action.car.request.SearchCarsRequest;
import org.example.action.car.request.GetCarsPageRequest;
import org.example.config.ApiTestBase;
import org.example.models.dto.CarDto;
import org.example.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class CarActionControllerApiTest extends ApiTestBase {

    @Autowired
    private CarService carService;

    private Long testCarId;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        cleanupTestData();
        // Create a test car for each test
        testCarId = createTestCar("Test Car");
    }

    // POST /actions/cars/get-all - Get all cars action
    @Test
    void getAllCars_shouldReturnAllCars_whenCarsExist() throws Exception {
        // Given - additional cars created
        createTestCar("BMW");
        createTestCar("Mercedes");

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/get-all"));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars.length()").value(3))
                .andExpect(jsonPath("$.cars[0].id").exists())
                .andExpect(jsonPath("$.cars[0].brand").exists());
    }

    @Test
    void getAllCars_shouldReturnEmptyList_whenNoCarsExist() throws Exception {
        // Given - clear all cars
        cleanupTestData();

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/get-all"));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars.length()").value(0));
    }

    // POST /actions/cars/get-by-id - Get car by ID action
    @Test
    void getCarById_shouldReturnCar_whenCarExists() throws Exception {
        // Given
        GetCarByIdRequest request = new GetCarByIdRequest(testCarId);

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/get-by-id", request));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.car.id").value(testCarId))
                .andExpect(jsonPath("$.car.brand").value("Test Car"));
    }

    @Test
    void getCarById_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given
        GetCarByIdRequest request = new GetCarByIdRequest(999L);

        // When & Then
        ResultActions result = validateNotFound(postJson("/actions/cars/get-by-id", request));
        expectActionFailure(result);
        expectActionMessage(result, "Car not found with id: 999");
    }

    // POST /actions/cars/create - Create car action
    @Test
    void createCar_shouldReturnCreatedCar_whenValidDataProvided() throws Exception {
        // Given
        CarDto carDto = new CarDto();
        carDto.setBrand("New Car");

        // When & Then
        ResultActions result = validateCreated(postJson("/actions/cars/create", carDto));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.car.id").exists())
                .andExpect(jsonPath("$.car.brand").value("New Car"));
    }

    @ParameterizedTest(name = "Create car validation: {0}")
    @MethodSource("invalidCarCreationData")
    void createCar_shouldReturn400_whenInvalidDataProvided(String testName, String brand, String expectedError) throws Exception {
        // Given
        CarDto invalidDto = new CarDto();
        if (brand != null) {
            invalidDto.setBrand(brand);
        }

        // When & Then
        validateBadRequest(postJson("/actions/cars/create", invalidDto))
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

    // POST /actions/cars/update - Update car action
    @Test
    void updateCar_shouldReturnUpdatedCar_whenValidDataProvided() throws Exception {
        // Given
        UpdateCarRequest request = new UpdateCarRequest(testCarId, "Updated Car");

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/update", request));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.car.id").value(testCarId))
                .andExpect(jsonPath("$.car.brand").value("Updated Car"));
    }

    @Test
    void updateCar_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given
        UpdateCarRequest request = new UpdateCarRequest(999L, "Updated Car");

        // When & Then
        ResultActions result = validateNotFound(postJson("/actions/cars/update", request));
        expectActionFailure(result);
        expectActionMessage(result, "Car not found with id: 999");
    }

    // POST /actions/cars/delete - Delete car action
    @Test
    void deleteCar_shouldReturnSuccess_whenCarExists() throws Exception {
        // Given
        DeleteCarRequest request = new DeleteCarRequest(testCarId);

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/delete", request));
        expectActionSuccess(result);
    }

    @Test
    void deleteCar_shouldReturn404_whenCarDoesNotExist() throws Exception {
        // Given
        DeleteCarRequest request = new DeleteCarRequest(999L);

        // When & Then
        ResultActions result = validateNotFound(postJson("/actions/cars/delete", request));
        expectActionFailure(result);
        expectActionMessage(result, "Car not found with id: 999");
    }

    // POST /actions/cars/search - Search cars action
    @Test
    void searchCars_shouldReturnMatchingCars_whenBrandProvided() throws Exception {
        // Given
        createTestCar("BMW X5");
        createTestCar("BMW M3");
        createTestCar("Mercedes");
        SearchCarsRequest request = new SearchCarsRequest("BMW");

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/search", request));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars.length()").value(2));
    }

    @Test
    void searchCars_shouldReturnAllCars_whenNullBrandProvided() throws Exception {
        // Given
        createTestCar("BMW");
        createTestCar("Mercedes");
        SearchCarsRequest request = new SearchCarsRequest(null);

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/search", request));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars.length()").value(3)); // 2 new + 1 test car
    }

    // POST /actions/cars/get-page - Get paginated cars action
    @Test
    void getCarsPage_shouldReturnPagedResults_whenValidParametersProvided() throws Exception {
        // Given
        createTestCar("Car 1");
        createTestCar("Car 2");
        createTestCar("Car 3");
        GetCarsPageRequest request = new GetCarsPageRequest(0, 2);

        // When & Then
        ResultActions result = validateSuccess(postJson("/actions/cars/get-page", request));
        expectActionSuccess(result)
                .andExpect(jsonPath("$.cars").isArray())
                .andExpect(jsonPath("$.cars.length()").value(2));
    }

    @ParameterizedTest(name = "Invalid pagination: {0}")
    @MethodSource("invalidPaginationData")
    void getCarsPage_shouldReturn400_whenInvalidParametersProvided(String testName, int page, int size, String expectedMessage) throws Exception {
        // Given
        GetCarsPageRequest request = new GetCarsPageRequest(page, size);

        // When & Then
        ResultActions result = validateBadRequest(postJson("/actions/cars/get-page", request));
        expectActionFailure(result);
        expectActionMessage(result, expectedMessage);
    }

    static Stream<Arguments> invalidPaginationData() {
        return Stream.of(
                Arguments.of("Negative page", -1, 10, "Page must be >= 0 and size must be > 0"),
                Arguments.of("Zero size", 0, 0, "Page must be >= 0 and size must be > 0"),
                Arguments.of("Negative size", 0, -1, "Page must be >= 0 and size must be > 0")
        );
    }
} 