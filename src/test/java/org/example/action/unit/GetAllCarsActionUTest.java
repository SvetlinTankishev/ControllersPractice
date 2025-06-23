package org.example.action.unit;

import org.example.action.car.GetAllCarsAction;
import org.example.action.car.request.GetAllCarsRequest;
import org.example.action.car.response.GetAllCarsResponse;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllCarsActionUTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private GetAllCarsAction getAllCarsAction;

    @Test
    void execute_shouldReturnAllCars_whenServiceReturnsData() {
        // Given
        List<Car> expectedCars = Arrays.asList(
            new Car(1L, "Toyota"),
            new Car(2L, "BMW")
        );
        when(carService.getAll()).thenReturn(expectedCars);
        GetAllCarsRequest request = new GetAllCarsRequest();

        // When
        GetAllCarsResponse response = getAllCarsAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCars()).hasSize(2);
        assertThat(response.getCars()).containsExactlyElementsOf(expectedCars);
        verify(carService).getAll();
    }

    @Test
    void execute_shouldReturnEmptyList_whenServiceReturnsEmptyList() {
        // Given
        List<Car> emptyCars = List.of();
        when(carService.getAll()).thenReturn(emptyCars);
        GetAllCarsRequest request = new GetAllCarsRequest();

        // When
        GetAllCarsResponse response = getAllCarsAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCars()).isEmpty();
        verify(carService).getAll();
    }

    @Test
    void getActionType_shouldReturnCorrectType() {
        // When
        String actionType = getAllCarsAction.getActionType();

        // Then
        assertThat(actionType).isEqualTo("GET_ALL_CARS");
    }
} 