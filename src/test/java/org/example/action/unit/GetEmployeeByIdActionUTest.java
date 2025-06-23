package org.example.action.unit;

import org.example.action.govemployee.GetEmployeeByIdAction;
import org.example.action.govemployee.request.GetEmployeeByIdRequest;
import org.example.action.govemployee.response.GetEmployeeByIdResponse;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
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
class GetEmployeeByIdActionUTest {

    @Mock
    private GovEmployeeService govEmployeeService;

    @InjectMocks
    private GetEmployeeByIdAction getEmployeeByIdAction;

    private GovEmployee createMockEmployee(Long id, String name) {
        GovEmployee employee = new GovEmployee();
        employee.setId(id);
        employee.setName(name);
        return employee;
    }

    @Test
    void execute_shouldReturnEmployee_whenEmployeeExists() {
        // Given
        Long employeeId = 1L;
        GovEmployee mockEmployee = createMockEmployee(employeeId, "John Doe");
        when(govEmployeeService.getById(employeeId)).thenReturn(Optional.of(mockEmployee));
        GetEmployeeByIdRequest request = new GetEmployeeByIdRequest(employeeId);

        // When
        GetEmployeeByIdResponse response = getEmployeeByIdAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getEmployee()).isNotNull();
        assertThat(response.getEmployee().getId()).isEqualTo(employeeId);
        assertThat(response.getEmployee().getName()).isEqualTo("John Doe");
        assertThat(response.getMessage()).isNull();
        verify(govEmployeeService).getById(employeeId);
    }

    @Test
    void execute_shouldReturnFailure_whenEmployeeDoesNotExist() {
        // Given
        Long employeeId = 999L;
        when(govEmployeeService.getById(employeeId)).thenReturn(Optional.empty());
        GetEmployeeByIdRequest request = new GetEmployeeByIdRequest(employeeId);

        // When
        GetEmployeeByIdResponse response = getEmployeeByIdAction.execute(request);

        // Then
        assertThat(response.isSuccess()).isFalse();
        assertThat(response.getEmployee()).isNull();
        assertThat(response.getMessage()).isEqualTo("Employee not found with id: " + employeeId);
        verify(govEmployeeService).getById(employeeId);
    }
} 