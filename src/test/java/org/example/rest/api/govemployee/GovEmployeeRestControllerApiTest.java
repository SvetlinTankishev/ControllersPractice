package org.example.rest.api.govemployee;

import org.example.config.ApiTestBase;
import org.example.models.dto.GovEmployeeDto;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GovEmployeeRestControllerApiTest extends ApiTestBase {

    @Autowired
    private GovEmployeeService govEmployeeService;

    private GovEmployee testEmployee;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        cleanupTestData();
        // Create a test employee for each test
        testEmployee = govEmployeeService.add("Test Employee");
    }

    // GET /api/employees - Get all employees
    @Test
    void getAllEmployees_shouldReturnAllEmployees_whenEmployeesExist() throws Exception {
        // Given - additional employees created in setUp()
        govEmployeeService.add("Alice");
        govEmployeeService.add("Bob");

        // When & Then
        validateSuccess(get("/api/employees"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    void getAllEmployees_shouldReturnEmptyList_whenNoEmployeesExist() throws Exception {
        // Given - clear all employees
        cleanupTestData();

        // When & Then
        validateSuccess(get("/api/employees"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // GET /api/employees/{id} - Get employee by ID
    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() throws Exception {
        // Given - testEmployee created in setUp()

        // When & Then
        validateSuccess(get("/api/employees/" + testEmployee.getId()))
                .andExpect(jsonPath("$.id").value(testEmployee.getId()))
                .andExpect(jsonPath("$.name").value("Test Employee"));
    }

    @Test
    void getEmployeeById_shouldReturn404_whenEmployeeDoesNotExist() throws Exception {
        // Given - non-existent ID
        Long nonExistentId = 999L;

        // When & Then
        validateNotFound(get("/api/employees/" + nonExistentId));
    }

    @Test
    void getEmployeeById_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given - invalid ID format
        String invalidId = "invalid";

        // When & Then
        validateBadRequest(get("/api/employees/" + invalidId))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
    }

    // POST /api/employees - Create new employee
    @Test
    void createEmployee_shouldReturnCreatedEmployee_whenValidDataProvided() throws Exception {
        // Given
        GovEmployeeDto employeeDto = new GovEmployeeDto();
        employeeDto.setName("New Employee");

        // When & Then
        validateCreated(post("/api/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Employee"));
    }

    @Test
    void createEmployee_shouldReturn400_whenInvalidJsonProvided() throws Exception {
        // Given - invalid JSON
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(post("/api/employees")
                .contentType("application/json")
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid JSON format"));
    }

    // Parameterized test for validation scenarios
    @ParameterizedTest(name = "Create employee validation: {0}")
    @MethodSource("invalidEmployeeCreationData")
    void createEmployee_shouldReturn400_whenInvalidDataProvided(String testName, String name, String expectedError) throws Exception {
        // Given
        GovEmployeeDto invalidDto = new GovEmployeeDto();
        if (name != null) {
            invalidDto.setName(name);
        }

        // When & Then
        validateBadRequest(post("/api/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value(expectedError));
    }

    static Stream<Arguments> invalidEmployeeCreationData() {
        return Stream.of(
                Arguments.of("Empty name", null, "name: Name cannot be blank"),
                Arguments.of("Name too short", "A", "name: Name must be between 2 and 100 characters"),
                Arguments.of("Name too long", "A".repeat(101), "name: Name must be between 2 and 100 characters"),
                Arguments.of("Name with spaces only", "   ", "name: Name cannot be blank")
        );
    }

    // PATCH /api/employees/{id} - Update employee
    @Test
    void updateEmployee_shouldReturnUpdatedEmployee_whenValidDataProvided() throws Exception {
        // Given
        GovEmployeeDto updateDto = new GovEmployeeDto();
        updateDto.setName("Updated Employee");

        // When & Then
        validateSuccess(patch("/api/employees/" + testEmployee.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(jsonPath("$.id").value(testEmployee.getId()))
                .andExpect(jsonPath("$.name").value("Updated Employee"));
    }

    @Test
    void updateEmployee_shouldReturn404_whenEmployeeDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        GovEmployeeDto updateDto = new GovEmployeeDto();
        updateDto.setName("Updated Employee");

        // When & Then
        validateNotFound(patch("/api/employees/" + nonExistentId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(updateDto)));
    }

    @Test
    void updateEmployee_shouldReturn400_whenNoNameProvided() throws Exception {
        // Given - empty DTO should fail validation
        GovEmployeeDto emptyDto = new GovEmployeeDto();

        // When & Then
        validateBadRequest(patch("/api/employees/" + testEmployee.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(emptyDto)))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value("name: Name cannot be blank"));
    }

    // Parameterized test for invalid ID scenarios
    @ParameterizedTest(name = "Invalid ID test: {0}")
    @ValueSource(strings = {"update", "delete"})
    void shouldReturn400_whenInvalidIdProvided(String operation) throws Exception {
        // Given
        String invalidId = "invalid";

        // When & Then
        switch (operation) {
            case "update":
                GovEmployeeDto updateDto = new GovEmployeeDto();
                updateDto.setName("Updated Employee");
                validateBadRequest(patch("/api/employees/" + invalidId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateDto)))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
            case "delete":
                validateBadRequest(delete("/api/employees/" + invalidId))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
        }
    }

    // DELETE /api/employees/{id} - Delete employee
    @Test
    void deleteEmployee_shouldReturn204_whenEmployeeExists() throws Exception {
        // Given - testEmployee created in setUp()

        // When & Then
        validateNoContent(delete("/api/employees/" + testEmployee.getId()));
    }

    @Test
    void deleteEmployee_shouldReturn404_whenEmployeeDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        validateNotFound(delete("/api/employees/" + nonExistentId));
    }

    // GET /api/employees/search - Search employees by name
    @Test
    void searchEmployees_shouldReturnMatchingEmployees_whenNameProvided() throws Exception {
        // Given
        govEmployeeService.add("Alice");
        govEmployeeService.add("Alice Johnson");
        govEmployeeService.add("Bob");

        // When & Then
        validateSuccess(get("/api/employees/search?name=alice"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void searchEmployees_shouldReturnAllEmployees_whenNoNameProvided() throws Exception {
        // Given - only testEmployee exists from setUp()

        // When & Then
        validateSuccess(get("/api/employees/search"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchEmployees_shouldReturnEmptyList_whenNoMatchingEmployees() throws Exception {
        // Given - no employees with "Charlie" name

        // When & Then
        validateSuccess(get("/api/employees/search?name=Charlie"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchEmployees_shouldBeCaseInsensitive() throws Exception {
        // Given
        govEmployeeService.add("ALICE");

        // When & Then
        validateSuccess(get("/api/employees/search?name=alice"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("ALICE"));
    }

    // GET /api/employees/page - Get paginated employees
    @Test
    void getEmployeesPage_shouldReturnPaginatedResults_whenValidParametersProvided() throws Exception {
        // Given
        govEmployeeService.add("Employee 1");
        govEmployeeService.add("Employee 2");
        govEmployeeService.add("Employee 3");

        // When & Then
        validateSuccess(get("/api/employees/page?page=0&size=2"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getEmployeesPage_shouldReturnEmptyList_whenPageOutOfBounds() throws Exception {
        // Given - only a few employees exist

        // When & Then
        validateSuccess(get("/api/employees/page?page=10&size=5"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // Parameterized test for pagination error scenarios
    @ParameterizedTest(name = "Pagination error: {0}")
    @MethodSource("invalidPaginationData")
    void getEmployeesPage_shouldReturn400_whenInvalidParametersProvided(String testName, String url, String expectedErrorCode, String expectedMessage) throws Exception {
        // When & Then
        var result = validateBadRequest(get(url))
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));
        
        if (expectedMessage != null) {
            result.andExpect(jsonPath("$.message").value(expectedMessage));
        }
    }

    static Stream<Arguments> invalidPaginationData() {
        return Stream.of(
                Arguments.of("Invalid size (0)", "/api/employees/page?page=0&size=0", "BAD_REQUEST", "Page must be >= 0 and size must be > 0"),
                Arguments.of("Missing page parameter", "/api/employees/page?size=5", "VALIDATION_ERROR", "Input validation failed"),
                Arguments.of("Invalid parameter types", "/api/employees/page?page=invalid&size=invalid", "BAD_REQUEST", null)
        );
    }
} 