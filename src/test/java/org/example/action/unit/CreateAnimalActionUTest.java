package org.example.action.unit;

import org.example.action.animal.CreateAnimalAction;
import org.example.action.animal.request.CreateAnimalRequest;
import org.example.action.animal.response.CreateAnimalResponse;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAnimalActionUTest {

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private CreateAnimalAction createAnimalAction;

    private Animal createMockAnimal(Long id, String type) {
        Animal animal = new Animal();
        animal.setId(id);
        animal.setType(type);
        return animal;
    }

    @Test
    void execute_shouldReturnCreatedAnimal_whenValidDataProvided() {
        // Given
        String animalType = "Dog";
        Animal mockAnimal = createMockAnimal(1L, animalType);
        when(animalService.add(animalType)).thenReturn(mockAnimal);
        CreateAnimalRequest request = new CreateAnimalRequest(animalType);

        // When
        CreateAnimalResponse response = createAnimalAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getAnimal()).isNotNull();
        assertThat(response.getAnimal().getId()).isEqualTo(1L);
        assertThat(response.getAnimal().getType()).isEqualTo(animalType);
        assertThat(response.getMessage()).isNull();
        verify(animalService).add(animalType);
    }

    @Test
    void execute_shouldReturnFailure_whenServiceThrowsException() {
        // Given
        String animalType = "Dog";
        when(animalService.add(animalType)).thenThrow(new RuntimeException("Database constraint violation"));
        CreateAnimalRequest request = new CreateAnimalRequest(animalType);

        // When
        CreateAnimalResponse response = createAnimalAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getAnimal()).isNull();
        assertThat(response.getMessage()).contains("Failed to create animal: Database constraint violation");
        verify(animalService).add(animalType);
    }

    @Test
    void execute_shouldReturnFailure_whenNullTypeProvided() {
        // Given
        CreateAnimalRequest request = new CreateAnimalRequest(null);
        when(animalService.add(null)).thenThrow(new IllegalArgumentException("Animal type cannot be null"));

        // When
        CreateAnimalResponse response = createAnimalAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getAnimal()).isNull();
        assertThat(response.getMessage()).contains("Failed to create animal: Animal type cannot be null");
        verify(animalService).add(null);
    }
} 