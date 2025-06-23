package org.example.action.api.animal;

import org.example.action.animal.request.*;
import org.example.config.ApiTestBase;
import org.example.models.dto.AnimalDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
public class AnimalActionControllerApiTest extends ApiTestBase {

    private Long testAnimalId;

    @BeforeEach
    void setUp() {
        cleanupTestData();
        testAnimalId = createTestAnimal("TestAnimal");
    }

    @Test
    void getAllAnimalsAction_shouldReturnAllAnimals_whenAnimalsExist() throws Exception {
        // Given - additional animals
        createTestAnimal("Dog");
        createTestAnimal("Cat");

        // When & Then
        mockMvc.perform(postJson("/actions/animals/get-all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.animals").isArray())
                .andExpect(jsonPath("$.animals.length()").value(3));
    }

    @Test
    void getAnimalByIdAction_shouldReturnAnimal_whenAnimalExists() throws Exception {
        // Given
        GetAnimalByIdRequest request = new GetAnimalByIdRequest(testAnimalId);

        // When & Then
        mockMvc.perform(postJson("/actions/animals/get-by-id", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.animal.id").value(testAnimalId))
                .andExpect(jsonPath("$.animal.type").value("TestAnimal"));
    }

    @Test
    void getAnimalByIdAction_shouldReturnFailure_whenAnimalDoesNotExist() throws Exception {
        // Given
        GetAnimalByIdRequest request = new GetAnimalByIdRequest(999L);

        // When & Then
        mockMvc.perform(postJson("/actions/animals/get-by-id", request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Animal not found with id: 999"));
    }

    @Test
    void createAnimalAction_shouldReturnCreatedAnimal_whenValidDataProvided() throws Exception {
        // Given
        AnimalDto animalDto = new AnimalDto();
        animalDto.setType("NewAnimal");

        // When & Then
        mockMvc.perform(postJson("/actions/animals/create", animalDto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.animal.id").exists())
                .andExpect(jsonPath("$.animal.type").value("NewAnimal"));
    }

    @Test
    void updateAnimalAction_shouldReturnUpdatedAnimal_whenValidDataProvided() throws Exception {
        // Given
        UpdateAnimalRequest request = new UpdateAnimalRequest(testAnimalId, "UpdatedAnimal");

        // When & Then
        mockMvc.perform(postJson("/actions/animals/update", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.animal.id").value(testAnimalId))
                .andExpect(jsonPath("$.animal.type").value("UpdatedAnimal"));
    }

    @Test
    void deleteAnimalAction_shouldReturnSuccess_whenAnimalExists() throws Exception {
        // Given
        DeleteAnimalRequest request = new DeleteAnimalRequest(testAnimalId);

        // When & Then
        mockMvc.perform(postJson("/actions/animals/delete", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Animal deleted successfully"));
    }

    @Test
    void searchAnimalsAction_shouldReturnMatchingAnimals_whenTypeProvided() throws Exception {
        // Given
        createTestAnimal("Dog");
        createTestAnimal("DogBreed");
        createTestAnimal("Cat");
        SearchAnimalsRequest request = new SearchAnimalsRequest("Dog");

        // When & Then
        mockMvc.perform(postJson("/actions/animals/search", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.animals").isArray())
                .andExpect(jsonPath("$.animals.length()").value(2));
    }

    @Test
    void getAnimalsPageAction_shouldReturnPaginatedResults_whenValidParametersProvided() throws Exception {
        // Given
        createTestAnimal("Animal1");
        createTestAnimal("Animal2");
        createTestAnimal("Animal3");
        GetAnimalsPageRequest request = new GetAnimalsPageRequest(0, 2);

        // When & Then
        mockMvc.perform(postJson("/actions/animals/get-page", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.animals").isArray())
                .andExpect(jsonPath("$.animals.length()").value(2));
    }
} 