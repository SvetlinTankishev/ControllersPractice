package org.example.rest.api.animal;

import org.example.config.ApiTestBase;
import org.example.models.dto.AnimalDto;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
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

public class AnimalRestControllerApiTest extends ApiTestBase {

    @Autowired
    private AnimalService animalService;

    private Animal testAnimal;

    @BeforeEach
    void setUp() {
        // Clean up any existing test data
        cleanupTestData();
        // Create a test animal for each test
        testAnimal = animalService.add("Test Animal");
    }

    // GET /api/animals - Get all animals
    @Test
    void getAllAnimals_shouldReturnAllAnimals_whenAnimalsExist() throws Exception {
        // Given - additional animals created in setUp()
        animalService.add("Lion");
        animalService.add("Tiger");

        // When & Then
        validateSuccess(getJson("/api/animals"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].type").exists());
    }

    @Test
    void getAllAnimals_shouldReturnEmptyList_whenNoAnimalsExist() throws Exception {
        // Given - clear all animals
        cleanupTestData();

        // When & Then
        validateSuccess(getJson("/api/animals"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // GET /api/animals/{id} - Get animal by ID
    @Test
    void getAnimalById_shouldReturnAnimal_whenAnimalExists() throws Exception {
        // Given - testAnimal created in setUp()

        // When & Then
        validateSuccess(getJson("/api/animals/" + testAnimal.getId()))
                .andExpect(jsonPath("$.id").value(testAnimal.getId()))
                .andExpect(jsonPath("$.type").value("Test Animal"));
    }

    @Test
    void getAnimalById_shouldReturn404_whenAnimalDoesNotExist() throws Exception {
        // Given - non-existent ID
        Long nonExistentId = 999L;

        // When & Then
        validateNotFound(getJson("/api/animals/" + nonExistentId));
    }

    @Test
    void getAnimalById_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given - invalid ID format
        String invalidId = "invalid";

        // When & Then
        validateBadRequest(getJson("/api/animals/" + invalidId))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
    }

    // POST /api/animals - Create new animal
    @Test
    void createAnimal_shouldReturnCreatedAnimal_whenValidDataProvided() throws Exception {
        // Given
        AnimalDto animalDto = new AnimalDto();
        animalDto.setType("New Animal");

        // When & Then
        validateCreated(postJson("/api/animals", animalDto))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").value("New Animal"));
    }

    @Test
    void createAnimal_shouldReturn400_whenInvalidJsonProvided() throws Exception {
        // Given - invalid JSON
        String invalidJson = "{ invalid json }";

        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/api/animals")
                .contentType("application/json")
                .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid JSON format"));
    }

    // Parameterized test for validation scenarios
    @ParameterizedTest(name = "Create animal validation: {0}")
    @MethodSource("invalidAnimalCreationData")
    void createAnimal_shouldReturn400_whenInvalidDataProvided(String testName, String type, String expectedError) throws Exception {
        // Given
        AnimalDto invalidDto = new AnimalDto();
        if (type != null) {
            invalidDto.setType(type);
        }

        // When & Then
        validateBadRequest(postJson("/api/animals", invalidDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value(expectedError));
    }

    static Stream<Arguments> invalidAnimalCreationData() {
        return Stream.of(
                Arguments.of("Empty type", null, "type: Type cannot be blank"),
                Arguments.of("Type too short", "A", "type: Type must be between 2 and 50 characters"),
                Arguments.of("Type too long", "A".repeat(51), "type: Type must be between 2 and 50 characters"),
                Arguments.of("Type with spaces only", "   ", "type: Type cannot be blank")
        );
    }

    // PATCH /api/animals/{id} - Update animal
    @Test
    void updateAnimal_shouldReturnUpdatedAnimal_whenValidDataProvided() throws Exception {
        // Given
        AnimalDto updateDto = new AnimalDto();
        updateDto.setType("Updated Animal");

        // When & Then
        validateSuccess(patchJson("/api/animals/" + testAnimal.getId(), updateDto))
                .andExpect(jsonPath("$.id").value(testAnimal.getId()))
                .andExpect(jsonPath("$.type").value("Updated Animal"));
    }

    @Test
    void updateAnimal_shouldReturn404_whenAnimalDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        AnimalDto updateDto = new AnimalDto();
        updateDto.setType("Updated Animal");

        // When & Then
        validateNotFound(patchJson("/api/animals/" + nonExistentId, updateDto));
    }

    @Test
    void updateAnimal_shouldReturn400_whenNoTypeProvided() throws Exception {
        // Given - empty DTO should fail validation
        AnimalDto emptyDto = new AnimalDto();

        // When & Then
        validateBadRequest(patchJson("/api/animals/" + testAnimal.getId(), emptyDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value("type: Type cannot be blank"));
    }

    // PUT /api/animals/{id} - Replace animal (with data safety)
    @Test
    void replaceAnimal_shouldReturnUpdatedAnimal_whenValidDataProvided() throws Exception {
        // Given
        AnimalDto replaceDto = new AnimalDto();
        replaceDto.setType("Replaced Animal");

        // When & Then
        validateSuccess(putJson("/api/animals/" + testAnimal.getId(), replaceDto))
                .andExpect(jsonPath("$.id").value(testAnimal.getId()))
                .andExpect(jsonPath("$.type").value("Replaced Animal"));
    }

    @Test
    void replaceAnimal_shouldReturn404_whenAnimalDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;
        AnimalDto replaceDto = new AnimalDto();
        replaceDto.setType("Replaced Animal");

        // When & Then
        validateNotFound(putJson("/api/animals/" + nonExistentId, replaceDto));
    }

    @Test
    void replaceAnimal_shouldReturn400_whenNoTypeProvided() throws Exception {
        // Given - empty DTO should fail validation to prevent data corruption
        AnimalDto emptyDto = new AnimalDto();

        // When & Then
        validateBadRequest(putJson("/api/animals/" + testAnimal.getId(), emptyDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value("type: Type cannot be blank"));
    }

    @Test
    void replaceAnimal_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given
        String invalidId = "invalid";
        AnimalDto replaceDto = new AnimalDto();
        replaceDto.setType("Replaced Animal");

        // When & Then
        validateBadRequest(putJson("/api/animals/" + invalidId, replaceDto))
                .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
    }

    @ParameterizedTest(name = "PUT animal validation: {0}")
    @MethodSource("invalidAnimalCreationData")
    void replaceAnimal_shouldReturn400_whenInvalidDataProvided(String testName, String type, String expectedError) throws Exception {
        // Given
        AnimalDto invalidDto = new AnimalDto();
        if (type != null) {
            invalidDto.setType(type);
        }

        // When & Then
        validateBadRequest(putJson("/api/animals/" + testAnimal.getId(), invalidDto))
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message").value("Input validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[0]").value(expectedError));
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
                AnimalDto updateDto = new AnimalDto();
                updateDto.setType("Updated Animal");
                validateBadRequest(patchJson("/api/animals/" + invalidId, updateDto))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
            case "replace":
                AnimalDto replaceDto = new AnimalDto();
                replaceDto.setType("Replaced Animal");
                validateBadRequest(putJson("/api/animals/" + invalidId, replaceDto))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
            case "delete":
                validateBadRequest(deleteJson("/api/animals/" + invalidId))
                        .andExpect(jsonPath("$.errorCode").value("BAD_REQUEST"))
                        .andExpect(jsonPath("$.message").value("Invalid value 'invalid' for parameter 'id'"));
                break;
        }
    }

    // DELETE /api/animals/{id} - Delete animal
    @Test
    void deleteAnimal_shouldReturn204_whenAnimalExists() throws Exception {
        // Given - testAnimal created in setUp()

        // When & Then
        validateNoContent(deleteJson("/api/animals/" + testAnimal.getId()));
    }

    @Test
    void deleteAnimal_shouldReturn404_whenAnimalDoesNotExist() throws Exception {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        validateNotFound(deleteJson("/api/animals/" + nonExistentId));
    }

    // GET /api/animals/search - Search animals by type
    @Test
    void searchAnimals_shouldReturnMatchingAnimals_whenTypeProvided() throws Exception {
        // Given
        animalService.add("Lion");
        animalService.add("Tiger");
        animalService.add("Elephant");

        // When & Then
        validateSuccess(getJson("/api/animals/search?type=lion"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("Lion"));
    }

    @Test
    void searchAnimals_shouldReturnAllAnimals_whenNoTypeProvided() throws Exception {
        // Given - only testAnimal exists from setUp()

        // When & Then
        validateSuccess(getJson("/api/animals/search"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void searchAnimals_shouldReturnEmptyList_whenNoMatchingAnimals() throws Exception {
        // Given - no animals with "zebra" type

        // When & Then
        validateSuccess(getJson("/api/animals/search?type=zebra"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void searchAnimals_shouldBeCaseInsensitive() throws Exception {
        // Given
        animalService.add("LION");

        // When & Then
        validateSuccess(getJson("/api/animals/search?type=lion"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value("LION"));
    }

    // GET /api/animals/page - Get paginated animals
    @Test
    void getAnimalsPage_shouldReturnPaginatedResults_whenValidParametersProvided() throws Exception {
        // Given
        animalService.add("Animal 1");
        animalService.add("Animal 2");
        animalService.add("Animal 3");

        // When & Then
        validateSuccess(getJson("/api/animals/page?page=0&size=2"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAnimalsPage_shouldReturnEmptyList_whenPageOutOfBounds() throws Exception {
        // Given - only a few animals exist

        // When & Then
        validateSuccess(getJson("/api/animals/page?page=10&size=5"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // Parameterized test for pagination error scenarios
    @ParameterizedTest(name = "Pagination error: {0}")
    @MethodSource("invalidPaginationData")
    void getAnimalsPage_shouldReturn400_whenInvalidParametersProvided(String testName, String url, String expectedErrorCode, String expectedMessage) throws Exception {
        // When & Then
        var result = validateBadRequest(getJson(url))
                .andExpect(jsonPath("$.errorCode").value(expectedErrorCode));
        
        if (expectedMessage != null) {
            result.andExpect(jsonPath("$.message").value(expectedMessage));
        }
    }

    static Stream<Arguments> invalidPaginationData() {
        return Stream.of(
                Arguments.of("Invalid size (0)", "/api/animals/page?page=0&size=0", "BAD_REQUEST", "Page must be >= 0 and size must be > 0"),
                Arguments.of("Missing page parameter", "/api/animals/page?size=5", "VALIDATION_ERROR", "Input validation failed"),
                Arguments.of("Invalid parameter types", "/api/animals/page?page=invalid&size=invalid", "BAD_REQUEST", null)
        );
    }
} 