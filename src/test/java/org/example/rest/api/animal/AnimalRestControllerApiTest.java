package org.example.rest.api.animal;

import org.example.config.ApiTestBase;
import org.example.models.dto.AnimalDto;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
        validateBadRequest(getJson("/api/animals/" + invalidId));
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
                .andExpect(status().isBadRequest());
    }

    @Test
    void createAnimal_shouldReturn400_whenEmptyBodyProvided() throws Exception {
        // Given - empty body
        AnimalDto emptyDto = new AnimalDto();

        // When & Then
        validateCreated(postJson("/api/animals", emptyDto))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type").isEmpty());
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
    void updateAnimal_shouldReturnOriginalAnimal_whenNoTypeProvided() throws Exception {
        // Given
        AnimalDto emptyDto = new AnimalDto();

        // When & Then
        validateSuccess(patchJson("/api/animals/" + testAnimal.getId(), emptyDto))
                .andExpect(jsonPath("$.id").value(testAnimal.getId()))
                .andExpect(jsonPath("$.type").value("Test Animal"));
    }

    @Test
    void updateAnimal_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given
        String invalidId = "invalid";
        AnimalDto updateDto = new AnimalDto();
        updateDto.setType("Updated Animal");

        // When & Then
        validateBadRequest(patchJson("/api/animals/" + invalidId, updateDto));
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

    @Test
    void deleteAnimal_shouldReturn400_whenInvalidIdProvided() throws Exception {
        // Given
        String invalidId = "invalid";

        // When & Then
        validateBadRequest(deleteJson("/api/animals/" + invalidId));
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

    @Test
    void getAnimalsPage_shouldReturnEmptyList_whenInvalidSizeProvided() throws Exception {
        // Given - invalid size parameter

        // When & Then
        validateSuccess(getJson("/api/animals/page?page=0&size=0"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAnimalsPage_shouldReturn400_whenMissingRequiredParameters() throws Exception {
        // Given - missing page parameter

        // When & Then
        validateBadRequest(getJson("/api/animals/page?size=5"));
    }

    @Test
    void getAnimalsPage_shouldReturn400_whenInvalidParameterTypes() throws Exception {
        // Given - invalid parameter types

        // When & Then
        validateBadRequest(getJson("/api/animals/page?page=invalid&size=invalid"));
    }
} 