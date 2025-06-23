package org.example.action.unit;

import org.example.action.animal.GetAnimalByIdAction;
import org.example.action.animal.request.GetAnimalByIdRequest;
import org.example.action.animal.response.GetAnimalByIdResponse;
import org.example.models.entity.Animal;
import org.example.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAnimalByIdActionUTest {

    @Mock
    private AnimalService animalService;

    @InjectMocks
    private GetAnimalByIdAction getAnimalByIdAction;

    private Animal createMockAnimal(Long id, String type) {
        Animal animal = new Animal();
        animal.setId(id);
        animal.setType(type);
        return animal;
    }

    @Test
    void execute_shouldReturnAnimal_whenAnimalExists() {
        // Given
        Long animalId = 1L;
        Animal mockAnimal = createMockAnimal(animalId, "Dog");
        when(animalService.getById(animalId)).thenReturn(Optional.of(mockAnimal));
        GetAnimalByIdRequest request = new GetAnimalByIdRequest(animalId);

        // When
        GetAnimalByIdResponse response = getAnimalByIdAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getAnimal()).isNotNull();
        assertThat(response.getAnimal().getId()).isEqualTo(animalId);
        assertThat(response.getAnimal().getType()).isEqualTo("Dog");
        assertThat(response.getMessage()).isNull();
        verify(animalService).getById(animalId);
    }

    @Test
    void execute_shouldReturnFailure_whenAnimalDoesNotExist() {
        // Given
        Long animalId = 999L;
        when(animalService.getById(animalId)).thenReturn(Optional.empty());
        GetAnimalByIdRequest request = new GetAnimalByIdRequest(animalId);

        // When
        GetAnimalByIdResponse response = getAnimalByIdAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getAnimal()).isNull();
        assertThat(response.getMessage()).isEqualTo("Animal not found with id: " + animalId);
        verify(animalService).getById(animalId);
    }
} 