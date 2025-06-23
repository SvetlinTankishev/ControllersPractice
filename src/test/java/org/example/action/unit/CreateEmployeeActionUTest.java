package org.example.action.unit;

import org.example.action.govemployee.CreateEmployeeAction;
import org.example.action.govemployee.request.CreateEmployeeRequest;
import org.example.action.govemployee.response.CreateEmployeeResponse;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateEmployeeActionUTest {

    @Mock
    private GovEmployeeService govEmployeeService;

    @InjectMocks
    private CreateEmployeeAction createEmployeeAction;

    private GovEmployee createMockEmployee(Long id, String name) {
        GovEmployee employee = new GovEmployee();
        employee.setId(id);
        employee.setName(name);
        return employee;
    }

    @Test
    void execute_shouldReturnCreatedEmployee_whenValidDataProvided() {
        // Given
        String employeeName = "John Doe";
        GovEmployee mockEmployee = createMockEmployee(1L, employeeName);
        when(govEmployeeService.add(employeeName)).thenReturn(mockEmployee);
        CreateEmployeeRequest request = new CreateEmployeeRequest(employeeName);

        // When
        CreateEmployeeResponse response = createEmployeeAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getEmployee()).isNotNull();
        assertThat(response.getEmployee().getId()).isEqualTo(1L);
        assertThat(response.getEmployee().getName()).isEqualTo(employeeName);
        assertThat(response.getMessage()).isNull();
        verify(govEmployeeService).add(employeeName);
    }

    @Test
    void execute_shouldReturnFailure_whenServiceThrowsException() {
        // Given
        String employeeName = "John Doe";
        when(govEmployeeService.add(employeeName)).thenThrow(new RuntimeException("Database constraint violation"));
        CreateEmployeeRequest request = new CreateEmployeeRequest(employeeName);

        // When
        CreateEmployeeResponse response = createEmployeeAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getEmployee()).isNull();
        assertThat(response.getMessage()).contains("Failed to create employee: Database constraint violation");
        verify(govEmployeeService).add(employeeName);
    }

    @Test
    void execute_shouldReturnFailure_whenNullNameProvided() {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest(null);
        when(govEmployeeService.add(null)).thenThrow(new IllegalArgumentException("Employee name cannot be null"));

        // When
        CreateEmployeeResponse response = createEmployeeAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getEmployee()).isNull();
        assertThat(response.getMessage()).contains("Failed to create employee: Employee name cannot be null");
        verify(govEmployeeService).add(null);
    }
} 