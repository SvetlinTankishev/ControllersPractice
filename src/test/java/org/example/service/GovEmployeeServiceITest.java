package org.example.service;

import org.example.models.entity.GovEmployee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.example.config.IntegrationTestBase;

@SpringBootTest
@Transactional
public class GovEmployeeServiceITest extends IntegrationTestBase {
    @Autowired
    private GovEmployeeService govEmployeeService;

    @Test
    void addAndGetEmployee_shouldCreateAndRetrieveEmployee_whenValidDataProvided() {
        // Given
        String name = "Alice Johnson";
        
        // When
        GovEmployee savedEmployee = govEmployeeService.add(name);
        Optional<GovEmployee> retrievedEmployee = govEmployeeService.getById(savedEmployee.getId());
        
        // Then
        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo(name);
        assertThat(retrievedEmployee).isPresent();
        assertThat(retrievedEmployee.get().getName()).isEqualTo(name);
        assertThat(retrievedEmployee.get().getCreatedAt()).isNotNull();
        assertThat(retrievedEmployee.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void deleteEmployee_shouldRemoveEmployee_whenEmployeeExists() {
        // Given
        GovEmployee savedEmployee = govEmployeeService.add("John Smith");
        
        // When
        boolean deleted = govEmployeeService.delete(savedEmployee.getId());
        Optional<GovEmployee> retrievedEmployee = govEmployeeService.getById(savedEmployee.getId());
        
        // Then
        assertThat(deleted).isTrue();
        assertThat(retrievedEmployee).isEmpty();
    }

    @Test
    void searchByName_shouldReturnMatchingEmployees_whenNameExists() {
        // Given
        govEmployeeService.add("Alice Johnson");
        govEmployeeService.add("Alice Smith");
        govEmployeeService.add("Bob Jones");
        
        // When
        List<GovEmployee> aliceEmployees = govEmployeeService.searchByName("Alice");
        
        // Then
        assertThat(aliceEmployees).hasSize(2);
        assertThat(aliceEmployees).allMatch(employee -> employee.getName().contains("Alice"));
    }

    @Test
    void updateEmployee_shouldModifyName_whenEmployeeExists() {
        // Given
        GovEmployee originalEmployee = govEmployeeService.add("John Doe");
        String newName = "John Smith";
        
        // When
        Optional<GovEmployee> updatedEmployee = govEmployeeService.update(originalEmployee.getId(), newName);
        
        // Then
        assertThat(updatedEmployee).isPresent();
        assertThat(updatedEmployee.get().getName()).isEqualTo(newName);
        assertThat(updatedEmployee.get().getId()).isEqualTo(originalEmployee.getId());
    }

    @Test
    void getPage_shouldReturnPaginatedResults_whenValidParametersProvided() {
        // Given
        govEmployeeService.add("Alice Johnson");
        govEmployeeService.add("Bob Smith");
        govEmployeeService.add("Charlie Brown");
        
        // When
        List<GovEmployee> firstPage = govEmployeeService.getPage(0, 2);
        
        // Then
        assertThat(firstPage).hasSizeLessThanOrEqualTo(2);
    }
} 