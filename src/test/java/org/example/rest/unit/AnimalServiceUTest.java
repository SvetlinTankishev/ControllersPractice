package org.example.rest.unit;

import org.example.models.entity.Animal;
import org.example.repository.AnimalRepository;
import org.example.service.AnimalService;
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
class AnimalServiceUTest {
    @Mock
    private AnimalRepository animalRepository;
    
    @InjectMocks
    private AnimalService animalService;

    @Test
    void getAll_shouldReturnAllAnimals_whenRepositoryReturnsData() {
        // Given
        List<Animal> expectedAnimals = Arrays.asList(
            new Animal(1L, "Lion"), 
            new Animal(2L, "Tiger")
        );
        when(animalRepository.findAll()).thenReturn(expectedAnimals);
        
        // When
        List<Animal> actualAnimals = animalService.getAll();
        
        // Then
        assertThat(actualAnimals).hasSize(2);
        assertThat(actualAnimals).containsExactlyElementsOf(expectedAnimals);
        verify(animalRepository).findAll();
    }

    @ParameterizedTest(name = "Get by ID: {0}")
    @MethodSource("getByIdData")
    void getById_shouldHandleVariousScenarios(String testName, Long animalId, Optional<Animal> repositoryResult, boolean shouldBePresent, String expectedType) {
        // Given
        when(animalRepository.findById(animalId)).thenReturn(repositoryResult);
        
        // When
        Optional<Animal> result = animalService.getById(animalId);
        
        // Then
        assertThat(result.isPresent()).isEqualTo(shouldBePresent);
        if (shouldBePresent) {
            assertThat(result.get().getType()).isEqualTo(expectedType);
        }
        verify(animalRepository).findById(animalId);
    }

    static Stream<Arguments> getByIdData() {
        return Stream.of(
                Arguments.of("Animal exists", 1L, Optional.of(new Animal(1L, "Lion")), true, "Lion"),
                Arguments.of("Animal does not exist", 999L, Optional.empty(), false, null)
        );
    }

    @Test
    void add_shouldReturnSavedAnimal_whenValidTypeProvided() {
        // Given
        String type = "Elephant";
        Animal savedAnimal = new Animal(3L, type);
        when(animalRepository.save(any(Animal.class))).thenReturn(savedAnimal);
        
        // When
        Animal result = animalService.add(type);
        
        // Then
        assertThat(result.getType()).isEqualTo(type);
        assertThat(result.getId()).isEqualTo(3L);
        verify(animalRepository).save(any(Animal.class));
    }

    @ParameterizedTest(name = "Delete animal: {0}")
    @MethodSource("deleteData")
    void delete_shouldHandleVariousScenarios(String testName, Long animalId, boolean existsInRepo, boolean expectedResult, boolean shouldCallDelete) {
        // Given
        when(animalRepository.existsById(animalId)).thenReturn(existsInRepo);
        if (shouldCallDelete) {
            doNothing().when(animalRepository).deleteById(animalId);
        }
        
        // When
        boolean result = animalService.delete(animalId);
        
        // Then
        assertThat(result).isEqualTo(expectedResult);
        verify(animalRepository).existsById(animalId);
        if (shouldCallDelete) {
            verify(animalRepository).deleteById(animalId);
        } else {
            verify(animalRepository, never()).deleteById(animalId);
        }
    }

    static Stream<Arguments> deleteData() {
        return Stream.of(
                Arguments.of("Animal exists", 1L, true, true, true),
                Arguments.of("Animal does not exist", 999L, false, false, false)
        );
    }

    @ParameterizedTest(name = "Update animal: {0}")
    @MethodSource("updateData")
    void update_shouldHandleVariousScenarios(String testName, Long animalId, Optional<Animal> repositoryResult, String newType, boolean shouldBePresent, boolean shouldCallSave) {
        // Given
        when(animalRepository.findById(animalId)).thenReturn(repositoryResult);
        if (shouldCallSave && repositoryResult.isPresent()) {
            Animal updatedAnimal = new Animal(animalId, newType);
            when(animalRepository.save(repositoryResult.get())).thenReturn(updatedAnimal);
        }
        
        // When
        Optional<Animal> result = animalService.update(animalId, newType);
        
        // Then
        assertThat(result.isPresent()).isEqualTo(shouldBePresent);
        if (shouldBePresent) {
            assertThat(result.get().getType()).isEqualTo(newType);
        }
        verify(animalRepository).findById(animalId);
        if (shouldCallSave) {
            verify(animalRepository).save(any(Animal.class));
        } else {
            verify(animalRepository, never()).save(any(Animal.class));
        }
    }

    static Stream<Arguments> updateData() {
        return Stream.of(
                Arguments.of("Animal exists", 1L, Optional.of(new Animal(1L, "Old Type")), "Updated Type", true, true),
                Arguments.of("Animal does not exist", 999L, Optional.empty(), "Updated Type", false, false)
        );
    }

    @ParameterizedTest(name = "Search by type: {0}")
    @MethodSource("searchByTypeData")
    void searchByType_shouldHandleVariousScenarios(String testName, String searchType, List<Animal> expectedAnimals, boolean shouldCallSpecificSearch) {
        // Given
        if (shouldCallSpecificSearch) {
            when(animalRepository.findByTypeContainingIgnoreCase(searchType)).thenReturn(expectedAnimals);
        } else {
            when(animalRepository.findAll()).thenReturn(expectedAnimals);
        }
        
        // When
        List<Animal> result = animalService.searchByType(searchType);
        
        // Then
        assertThat(result).hasSize(expectedAnimals.size());
        assertThat(result).containsExactlyElementsOf(expectedAnimals);
        
        if (shouldCallSpecificSearch) {
            verify(animalRepository).findByTypeContainingIgnoreCase(searchType);
            verify(animalRepository, never()).findAll();
        } else {
            verify(animalRepository).findAll();
            verify(animalRepository, never()).findByTypeContainingIgnoreCase(any());
        }
    }

    static Stream<Arguments> searchByTypeData() {
        List<Animal> lionAnimals = Arrays.asList(new Animal(1L, "Lion"));
        List<Animal> allAnimals = Arrays.asList(new Animal(1L, "Lion"), new Animal(2L, "Tiger"));
        List<Animal> emptyAnimals = Arrays.asList(); // No animals match "   "
        
        return Stream.of(
                Arguments.of("Specific type provided", "Lion", lionAnimals, true),
                Arguments.of("Type is null", null, allAnimals, false),
                Arguments.of("Type is empty", "", allAnimals, false),
                Arguments.of("Type is blank spaces", "   ", emptyAnimals, true)
        );
    }

    @Test
    void getPage_shouldReturnPagedResults_whenValidParametersProvided() {
        // Given
        int page = 0;
        int size = 5;
        List<Animal> expectedAnimals = Arrays.asList(new Animal(1L, "Lion"), new Animal(2L, "Tiger"));
        org.springframework.data.domain.PageRequest pageRequest = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<Animal> mockPage = mock(org.springframework.data.domain.Page.class);
        when(animalRepository.findAll(pageRequest)).thenReturn(mockPage);
        when(mockPage.getContent()).thenReturn(expectedAnimals);
        
        // When
        List<Animal> result = animalService.getPage(page, size);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedAnimals);
        verify(animalRepository).findAll(pageRequest);
        verify(mockPage).getContent();
    }
} 