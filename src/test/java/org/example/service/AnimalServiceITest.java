package org.example.service;

import org.example.models.entity.Animal;
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
public class AnimalServiceITest extends IntegrationTestBase {
    @Autowired
    private AnimalService animalService;

    @Test
    void addAndGetAnimal_shouldCreateAndRetrieveAnimal_whenValidDataProvided() {
        // Given
        String type = "parrot";
        
        // When
        Animal savedAnimal = animalService.add(type);
        Optional<Animal> retrievedAnimal = animalService.getById(savedAnimal.getId());
        
        // Then
        assertThat(savedAnimal.getId()).isNotNull();
        assertThat(savedAnimal.getType()).isEqualTo(type);
        assertThat(retrievedAnimal).isPresent();
        assertThat(retrievedAnimal.get().getType()).isEqualTo(type);
        assertThat(retrievedAnimal.get().getCreatedAt()).isNotNull();
        assertThat(retrievedAnimal.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void deleteAnimal_shouldRemoveAnimal_whenAnimalExists() {
        // Given
        Animal savedAnimal = animalService.add("cat");
        
        // When
        boolean deleted = animalService.delete(savedAnimal.getId());
        Optional<Animal> retrievedAnimal = animalService.getById(savedAnimal.getId());
        
        // Then
        assertThat(deleted).isTrue();
        assertThat(retrievedAnimal).isEmpty();
    }

    @Test
    void searchByType_shouldReturnMatchingAnimals_whenTypeExists() {
        // Given
        animalService.add("lion");
        animalService.add("lioness");
        animalService.add("cat");
        
        // When
        List<Animal> lions = animalService.searchByType("lion");
        
        // Then
        assertThat(lions).hasSize(2);
        assertThat(lions).allMatch(animal -> animal.getType().contains("lion"));
    }

    @Test
    void updateAnimal_shouldModifyType_whenAnimalExists() {
        // Given
        Animal originalAnimal = animalService.add("cat");
        String newType = "tiger";
        
        // When
        Optional<Animal> updatedAnimal = animalService.update(originalAnimal.getId(), newType);
        
        // Then
        assertThat(updatedAnimal).isPresent();
        assertThat(updatedAnimal.get().getType()).isEqualTo(newType);
        assertThat(updatedAnimal.get().getId()).isEqualTo(originalAnimal.getId());
    }

    @Test
    void getPage_shouldReturnPaginatedResults_whenValidParametersProvided() {
        // Given
        animalService.add("cat");
        animalService.add("dog");
        animalService.add("bird");
        
        // When
        List<Animal> firstPage = animalService.getPage(0, 2);
        
        // Then
        assertThat(firstPage).hasSizeLessThanOrEqualTo(2);
    }
} 