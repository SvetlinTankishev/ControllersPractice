package org.example.action.integration;

import org.example.action.car.request.GetAllCarsRequest;
import org.example.action.car.request.GetCarByIdRequest;
import org.example.action.car.request.CreateCarRequest;
import org.example.action.car.response.GetAllCarsResponse;
import org.example.action.car.response.GetCarByIdResponse;
import org.example.action.car.response.CreateCarResponse;
import org.example.action.core.ActionDispatcher;
import org.example.config.IntegrationTestBase;
import org.example.service.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ActionDispatcherITest extends IntegrationTestBase {

    @Autowired
    private ActionDispatcher actionDispatcher;

    @Autowired
    private CarService carService;

    private Long testCarId;

    @BeforeEach
    void setUp() {
        // Create test data
        testCarId = carService.add("Test Car").getId();
    }

    @Test
    void dispatch_shouldExecuteGetAllCarsAction_whenValidRequestProvided() {
        // Given
        GetAllCarsRequest request = new GetAllCarsRequest();

        // When
        GetAllCarsResponse response = actionDispatcher.dispatch(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCars()).isNotEmpty();
        assertThat(response.getCars().get(0).getBrand()).isEqualTo("Test Car");
    }

    @Test
    void dispatch_shouldExecuteGetCarByIdAction_whenValidRequestProvided() {
        // Given
        GetCarByIdRequest request = new GetCarByIdRequest(testCarId);

        // When
        GetCarByIdResponse response = actionDispatcher.dispatch(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCar()).isNotNull();
        assertThat(response.getCar().getId()).isEqualTo(testCarId);
        assertThat(response.getCar().getBrand()).isEqualTo("Test Car");
    }

    @Test
    void dispatch_shouldExecuteCreateCarAction_whenValidRequestProvided() {
        // Given
        CreateCarRequest request = new CreateCarRequest("New Car");

        // When
        CreateCarResponse response = actionDispatcher.dispatch(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getCar()).isNotNull();
        assertThat(response.getCar().getBrand()).isEqualTo("New Car");
        assertThat(response.getCar().getId()).isNotNull();
    }

    @Test
    void dispatch_shouldThrowException_whenUnknownActionTypeProvided() {
        // Given
        UnknownActionRequest unknownRequest = new UnknownActionRequest();

        // When & Then
        assertThatThrownBy(() -> actionDispatcher.dispatch(unknownRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No handler found for action type: UNKNOWN_ACTION");
    }

    @Test
    void hasHandler_shouldReturnTrue_whenKnownActionTypeProvided() {
        // When & Then
        assertThat(actionDispatcher.hasHandler("GET_ALL_CARS")).isTrue();
        assertThat(actionDispatcher.hasHandler("GET_CAR_BY_ID")).isTrue();
        assertThat(actionDispatcher.hasHandler("CREATE_CAR")).isTrue();
    }

    @Test
    void hasHandler_shouldReturnFalse_whenUnknownActionTypeProvided() {
        // When & Then
        assertThat(actionDispatcher.hasHandler("UNKNOWN_ACTION")).isFalse();
        assertThat(actionDispatcher.hasHandler("INVALID_ACTION")).isFalse();
    }

    // Helper class for testing unknown action
    private static class UnknownActionRequest extends org.example.action.core.ActionRequest {
        @Override
        public String getActionType() {
            return "UNKNOWN_ACTION";
        }
    }
} 