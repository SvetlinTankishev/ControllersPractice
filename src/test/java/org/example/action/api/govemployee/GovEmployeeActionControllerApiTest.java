package org.example.action.api.govemployee;

import org.example.action.govemployee.request.*;
import org.example.config.ApiTestBase;
import org.example.models.dto.GovEmployeeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@Transactional
public class GovEmployeeActionControllerApiTest extends ApiTestBase {

    private Long testEmployeeId;

    @BeforeEach
    void setUp() {
        cleanupTestData();
        testEmployeeId = createTestEmployee("TestEmployee");
    }

    @Test
    void getAllEmployeesAction_shouldReturnAllEmployees_whenEmployeesExist() throws Exception {
        // Given - additional employees
        createTestEmployee("Alice Johnson");
        createTestEmployee("Bob Smith");

        // When & Then
        mockMvc.perform(postJson("/actions/employees/get-all", new GetAllEmployeesRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.employees").isArray())
                .andExpect(jsonPath("$.employees.length()").value(3));
    }

    @Test
    void getEmployeeByIdAction_shouldReturnEmployee_whenEmployeeExists() throws Exception {
        // Given
        GetEmployeeByIdRequest request = new GetEmployeeByIdRequest(testEmployeeId);

        // When & Then
        mockMvc.perform(postJson("/actions/employees/get-by-id", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.employee.id").value(testEmployeeId))
                .andExpect(jsonPath("$.employee.name").value("TestEmployee"));
    }

    @Test
    void getEmployeeByIdAction_shouldReturnFailure_whenEmployeeDoesNotExist() throws Exception {
        // Given
        GetEmployeeByIdRequest request = new GetEmployeeByIdRequest(999L);

        // When & Then
        mockMvc.perform(postJson("/actions/employees/get-by-id", request))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Employee not found with id: 999"));
    }

    @Test
    void createEmployeeAction_shouldReturnCreatedEmployee_whenValidDataProvided() throws Exception {
        // Given
        GovEmployeeDto employeeDto = new GovEmployeeDto();
        employeeDto.setName("NewEmployee");

        // When & Then
        mockMvc.perform(postJson("/actions/employees/create", employeeDto))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.employee.id").exists())
                .andExpect(jsonPath("$.employee.name").value("NewEmployee"));
    }

    @Test
    void updateEmployeeAction_shouldReturnUpdatedEmployee_whenValidDataProvided() throws Exception {
        // Given
        UpdateEmployeeRequest request = new UpdateEmployeeRequest(testEmployeeId, "UpdatedEmployee");

        // When & Then
        mockMvc.perform(postJson("/actions/employees/update", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.employee.id").value(testEmployeeId))
                .andExpect(jsonPath("$.employee.name").value("UpdatedEmployee"));
    }

    @Test
    void deleteEmployeeAction_shouldReturnSuccess_whenEmployeeExists() throws Exception {
        // Given
        DeleteEmployeeRequest request = new DeleteEmployeeRequest(testEmployeeId);

        // When & Then
        mockMvc.perform(postJson("/actions/employees/delete", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Employee deleted successfully"));
    }

    @Test
    void searchEmployeesAction_shouldReturnMatchingEmployees_whenNameProvided() throws Exception {
        // Given
        createTestEmployee("John");
        createTestEmployee("John Smith");
        createTestEmployee("Alice");
        SearchEmployeesRequest request = new SearchEmployeesRequest("John");

        // When & Then
        mockMvc.perform(postJson("/actions/employees/search", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.employees").isArray())
                .andExpect(jsonPath("$.employees.length()").value(2));
    }

    @Test
    void getEmployeesPageAction_shouldReturnPaginatedResults_whenValidParametersProvided() throws Exception {
        // Given
        createTestEmployee("Employee1");
        createTestEmployee("Employee2");
        createTestEmployee("Employee3");
        GetEmployeesPageRequest request = new GetEmployeesPageRequest(0, 2);

        // When & Then
        mockMvc.perform(postJson("/actions/employees/get-page", request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.employees").isArray())
                .andExpect(jsonPath("$.employees.length()").value(2));
    }
} 