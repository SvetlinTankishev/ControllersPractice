package org.example.action.unit;

import org.example.action.car.CreateCarAction;
import org.example.action.car.request.CreateCarRequest;
import org.example.action.car.response.CreateCarResponse;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCarActionUTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CreateCarAction createCarAction;

    @Test
    void execute_shouldReturnCreatedCar_whenValidBrandProvided() {
        // Given
        String brand = "Honda";
        Car createdCar = new Car(3L, brand);
        when(carService.add(brand)).thenReturn(createdCar);
        CreateCarRequest request = new CreateCarRequest(brand);

        // When
        CreateCarResponse response = createCarAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCar()).isNotNull();
        assertThat(response.getCar().getBrand()).isEqualTo(brand);
        assertThat(response.getCar().getId()).isEqualTo(3L);
        assertThat(response.getMessage()).isNull();
        verify(carService).add(brand);
    }

    @Test
    void execute_shouldReturnFailure_whenServiceThrowsException() {
        // Given
        String brand = "Invalid";
        String errorMessage = "Database error";
        when(carService.add(brand)).thenThrow(new RuntimeException(errorMessage));
        CreateCarRequest request = new CreateCarRequest(brand);

        // When
        CreateCarResponse response = createCarAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getCar()).isNull();
        assertThat(response.getMessage()).isEqualTo("Failed to create car: " + errorMessage);
        verify(carService).add(brand);
    }

    @Test
    void getActionType_shouldReturnCorrectType() {
        // When
        String actionType = createCarAction.getActionType();

        // Then
        assertThat(actionType).isEqualTo("CREATE_CAR");
    }
} 