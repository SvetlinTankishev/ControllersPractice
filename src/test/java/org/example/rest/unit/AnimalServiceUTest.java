package org.example.rest.unit;

import org.example.models.entity.Animal;
import org.example.repository.AnimalRepository;
import org.example.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            new Animal(1L, "cat"), 
            new Animal(2L, "dog")
        );
        when(animalRepository.findAll()).thenReturn(expectedAnimals);
        
        // When
        List<Animal> actualAnimals = animalService.getAll();
        
        // Then
        assertThat(actualAnimals).hasSize(2);
        assertThat(actualAnimals).containsExactlyElementsOf(expectedAnimals);
        verify(animalRepository).findAll();
    }

    @Test
    void getById_shouldReturnOptionalWithAnimal_whenAnimalExists() {
        // Given
        Long animalId = 1L;
        Animal expectedAnimal = new Animal(animalId, "cat");
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(expectedAnimal));
        
        // When
        Optional<Animal> result = animalService.getById(animalId);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getType()).isEqualTo("cat");
        verify(animalRepository).findById(animalId);
    }

    @Test
    void getById_shouldReturnEmptyOptional_whenAnimalDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(animalRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // When
        Optional<Animal> result = animalService.getById(nonExistentId);
        
        // Then
        assertThat(result).isEmpty();
        verify(animalRepository).findById(nonExistentId);
    }

    @Test
    void add_shouldReturnSavedAnimal_whenValidTypeProvided() {
        // Given
        String type = "parrot";
        Animal savedAnimal = new Animal(3L, type);
        when(animalRepository.save(any(Animal.class))).thenReturn(savedAnimal);
        
        // When
        Animal result = animalService.add(type);
        
        // Then
        assertThat(result.getType()).isEqualTo(type);
        assertThat(result.getId()).isEqualTo(3L);
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void delete_shouldReturnTrue_whenAnimalExists() {
        // Given
        Long animalId = 1L;
        when(animalRepository.existsById(animalId)).thenReturn(true);
        doNothing().when(animalRepository).deleteById(animalId);
        
        // When
        boolean result = animalService.delete(animalId);
        
        // Then
        assertThat(result).isTrue();
        verify(animalRepository).existsById(animalId);
        verify(animalRepository).deleteById(animalId);
    }

    @Test
    void delete_shouldReturnFalse_whenAnimalDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(animalRepository.existsById(nonExistentId)).thenReturn(false);
        
        // When
        boolean result = animalService.delete(nonExistentId);
        
        // Then
        assertThat(result).isFalse();
        verify(animalRepository).existsById(nonExistentId);
        verify(animalRepository, never()).deleteById(nonExistentId);
    }

    @Test
    void update_shouldReturnOptionalWithUpdatedAnimal_whenAnimalExists() {
        // Given
        Long animalId = 1L;
        String newType = "Updated Type";
        Animal existingAnimal = new Animal(animalId, "Old Type");
        Animal updatedAnimal = new Animal(animalId, newType);
        
        when(animalRepository.findById(animalId)).thenReturn(Optional.of(existingAnimal));
        when(animalRepository.save(existingAnimal)).thenReturn(updatedAnimal);
        
        // When
        Optional<Animal> result = animalService.update(animalId, newType);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getType()).isEqualTo(newType);
        verify(animalRepository).findById(animalId);
        verify(animalRepository).save(existingAnimal);
    }

    @Test
    void update_shouldReturnEmptyOptional_whenAnimalDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        String newType = "Updated Type";
        when(animalRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // When
        Optional<Animal> result = animalService.update(nonExistentId, newType);
        
        // Then
        assertThat(result).isEmpty();
        verify(animalRepository).findById(nonExistentId);
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void searchByType_shouldReturnFilteredAnimals_whenTypeProvided() {
        // Given
        String searchType = "dog";
        List<Animal> expectedAnimals = Arrays.asList(new Animal(1L, "dog"));
        when(animalRepository.findByTypeContainingIgnoreCase(searchType)).thenReturn(expectedAnimals);
        
        // When
        List<Animal> result = animalService.searchByType(searchType);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactlyElementsOf(expectedAnimals);
        verify(animalRepository).findByTypeContainingIgnoreCase(searchType);
    }

    @Test
    void searchByType_shouldReturnAllAnimals_whenTypeIsNull() {
        // Given
        List<Animal> allAnimals = Arrays.asList(new Animal(1L, "cat"), new Animal(2L, "dog"));
        when(animalRepository.findAll()).thenReturn(allAnimals);
        
        // When
        List<Animal> result = animalService.searchByType(null);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(allAnimals);
        verify(animalRepository).findAll();
        verify(animalRepository, never()).findByTypeContainingIgnoreCase(any());
    }

    @Test
    void searchByType_shouldReturnAllAnimals_whenTypeIsEmpty() {
        // Given
        List<Animal> allAnimals = Arrays.asList(new Animal(1L, "cat"), new Animal(2L, "dog"));
        when(animalRepository.findAll()).thenReturn(allAnimals);
        
        // When
        List<Animal> result = animalService.searchByType("");
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(allAnimals);
        verify(animalRepository).findAll();
        verify(animalRepository, never()).findByTypeContainingIgnoreCase(any());
    }
} 