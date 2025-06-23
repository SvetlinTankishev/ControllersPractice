package org.example.action.unit;

import org.example.action.govemployee.GetAllEmployeesAction;
import org.example.action.govemployee.request.GetAllEmployeesRequest;
import org.example.action.govemployee.response.GetAllEmployeesResponse;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllEmployeesActionUTest {

    @Mock
    private GovEmployeeService govEmployeeService;

    @InjectMocks
    private GetAllEmployeesAction getAllEmployeesAction;

    private GovEmployee createMockEmployee(Long id, String name) {
        GovEmployee employee = new GovEmployee();
        employee.setId(id);
        employee.setName(name);
        return employee;
    }

    @Test
    void execute_shouldReturnAllEmployees_whenEmployeesExist() {
        // Given
        List<GovEmployee> mockEmployees = Arrays.asList(
                createMockEmployee(1L, "Alice Johnson"),
                createMockEmployee(2L, "Bob Smith"),
                createMockEmployee(3L, "Charlie Brown")
        );
        when(govEmployeeService.getAll()).thenReturn(mockEmployees);
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();

        // When
        GetAllEmployeesResponse response = getAllEmployeesAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getEmployees()).hasSize(3);
        assertThat(response.getEmployees().get(0).getName()).isEqualTo("Alice Johnson");
        assertThat(response.getEmployees().get(1).getName()).isEqualTo("Bob Smith");
        assertThat(response.getEmployees().get(2).getName()).isEqualTo("Charlie Brown");
        verify(govEmployeeService).getAll();
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoEmployeesExist() {
        // Given
        when(govEmployeeService.getAll()).thenReturn(Collections.emptyList());
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();

        // When
        GetAllEmployeesResponse response = getAllEmployeesAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getEmployees()).isEmpty();
        verify(govEmployeeService).getAll();
    }

    @Test
    void execute_shouldReturnFailure_whenServiceThrowsException() {
        // Given
        when(govEmployeeService.getAll()).thenThrow(new RuntimeException("Database connection error"));
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();

        // When
        GetAllEmployeesResponse response = getAllEmployeesAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getEmployees()).isNull();
        assertThat(response.getMessage()).isEqualTo("Database connection error");
        verify(govEmployeeService).getAll();
    }
} 