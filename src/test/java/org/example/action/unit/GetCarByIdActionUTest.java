package org.example.action.unit;

import org.example.action.car.GetCarByIdAction;
import org.example.action.car.request.GetCarByIdRequest;
import org.example.action.car.response.GetCarByIdResponse;
import org.example.models.entity.Car;
import org.example.service.CarService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCarByIdActionUTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private GetCarByIdAction getCarByIdAction;

    @ParameterizedTest(name = "Get car by ID: {0}")
    @MethodSource("getCarByIdData")
    void execute_shouldHandleVariousScenarios(String testName, Long carId, Optional<Car> serviceResult, boolean expectedSuccess, String expectedBrand, String expectedMessage) {
        // Given
        when(carService.getById(carId)).thenReturn(serviceResult);
        GetCarByIdRequest request = new GetCarByIdRequest(carId);

        // When
        GetCarByIdResponse response = getCarByIdAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isEqualTo(expectedSuccess);
        if (expectedSuccess) {
            assertThat(response.getCar()).isNotNull();
            assertThat(response.getCar().getBrand()).isEqualTo(expectedBrand);
            assertThat(response.getMessage()).isNull();
        } else {
            assertThat(response.getCar()).isNull();
            assertThat(response.getMessage()).isEqualTo(expectedMessage);
        }
        verify(carService).getById(carId);
    }

    static Stream<Arguments> getCarByIdData() {
        return Stream.of(
            Arguments.of("Car exists", 1L, Optional.of(new Car(1L, "Toyota")), true, "Toyota", null),
            Arguments.of("Car does not exist", 999L, Optional.empty(), false, null, "Car not found with id: 999")
        );
    }

    @Test
    void getActionType_shouldReturnCorrectType() {
        // When
        String actionType = getCarByIdAction.getActionType();

        // Then
        assertThat(actionType).isEqualTo("GET_CAR_BY_ID");
    }
} 