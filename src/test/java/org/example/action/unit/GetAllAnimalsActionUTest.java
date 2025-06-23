package org.example.action.unit;

import org.example.action.animal.GetAllAnimalsAction;
import org.example.action.animal.request.GetAllAnimalsRequest;
import org.example.action.animal.response.GetAllAnimalsResponse;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllAnimalsActionUTest {

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private GetAllAnimalsAction getAllAnimalsAction;

    private Animal createMockAnimal(Long id, String type) {
        Animal animal = new Animal();
        animal.setId(id);
        animal.setType(type);
        return animal;
    }

    @Test
    void execute_shouldReturnAllAnimals_whenAnimalsExist() {
        // Given
        List<Animal> mockAnimals = Arrays.asList(
                createMockAnimal(1L, "Dog"),
                createMockAnimal(2L, "Cat"),
                createMockAnimal(3L, "Bird")
        );
        when(animalService.getAll()).thenReturn(mockAnimals);
        GetAllAnimalsRequest request = new GetAllAnimalsRequest();

        // When
        GetAllAnimalsResponse response = getAllAnimalsAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getAnimals()).hasSize(3);
        assertThat(response.getAnimals().get(0).getType()).isEqualTo("Dog");
        assertThat(response.getAnimals().get(1).getType()).isEqualTo("Cat");
        assertThat(response.getAnimals().get(2).getType()).isEqualTo("Bird");
        assertThat(response.getMessage()).isNull();
        verify(animalService).getAll();
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoAnimalsExist() {
        // Given
        when(animalService.getAll()).thenReturn(Arrays.asList());
        GetAllAnimalsRequest request = new GetAllAnimalsRequest();

        // When
        GetAllAnimalsResponse response = getAllAnimalsAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getAnimals()).isEmpty();
        assertThat(response.getMessage()).isNull();
        verify(animalService).getAll();
    }

    @Test
    void execute_shouldReturnFailure_whenServiceThrowsException() {
        // Given
        when(animalService.getAll()).thenThrow(new RuntimeException("Database connection failed"));
        GetAllAnimalsRequest request = new GetAllAnimalsRequest();

        // When
        GetAllAnimalsResponse response = getAllAnimalsAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getAnimals()).isNull();
        assertThat(response.getMessage()).isEqualTo("Database connection failed");
        verify(animalService).getAll();
    }
} 