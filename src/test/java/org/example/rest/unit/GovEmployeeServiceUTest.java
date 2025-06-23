package org.example.rest.unit;

import org.example.models.entity.GovEmployee;
import org.example.repository.GovEmployeeRepository;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GovEmployeeServiceUTest {
    @Mock
    private GovEmployeeRepository govEmployeeRepository;
    
    @InjectMocks
    private GovEmployeeService govEmployeeService;

    @Test
    void getAll_shouldReturnAllEmployees_whenRepositoryReturnsData() {
        // Given
        List<GovEmployee> expectedEmployees = Arrays.asList(
            new GovEmployee(1L, "Alice"), 
            new GovEmployee(2L, "Bob")
        );
        when(govEmployeeRepository.findAll()).thenReturn(expectedEmployees);
        
        // When
        List<GovEmployee> actualEmployees = govEmployeeService.getAll();
        
        // Then
        assertThat(actualEmployees).hasSize(2);
        assertThat(actualEmployees).containsExactlyElementsOf(expectedEmployees);
        verify(govEmployeeRepository).findAll();
    }

    @Test
    void getById_shouldReturnOptionalWithEmployee_whenEmployeeExists() {
        // Given
        Long employeeId = 1L;
        GovEmployee expectedEmployee = new GovEmployee(employeeId, "Alice");
        when(govEmployeeRepository.findById(employeeId)).thenReturn(Optional.of(expectedEmployee));
        
        // When
        Optional<GovEmployee> result = govEmployeeService.getById(employeeId);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
        verify(govEmployeeRepository).findById(employeeId);
    }

    @Test
    void getById_shouldReturnEmptyOptional_whenEmployeeDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(govEmployeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // When
        Optional<GovEmployee> result = govEmployeeService.getById(nonExistentId);
        
        // Then
        assertThat(result).isEmpty();
        verify(govEmployeeRepository).findById(nonExistentId);
    }

    @Test
    void add_shouldReturnSavedEmployee_whenValidNameProvided() {
        // Given
        String name = "Charlie";
        GovEmployee savedEmployee = new GovEmployee(3L, name);
        when(govEmployeeRepository.save(any(GovEmployee.class))).thenReturn(savedEmployee);
        
        // When
        GovEmployee result = govEmployeeService.add(name);
        
        // Then
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getId()).isEqualTo(3L);
        verify(govEmployeeRepository).save(any(GovEmployee.class));
    }

    @Test
    void delete_shouldReturnTrue_whenEmployeeExists() {
        // Given
        Long employeeId = 1L;
        when(govEmployeeRepository.existsById(employeeId)).thenReturn(true);
        doNothing().when(govEmployeeRepository).deleteById(employeeId);
        
        // When
        boolean result = govEmployeeService.delete(employeeId);
        
        // Then
        assertThat(result).isTrue();
        verify(govEmployeeRepository).existsById(employeeId);
        verify(govEmployeeRepository).deleteById(employeeId);
    }

    @Test
    void delete_shouldReturnFalse_whenEmployeeDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(govEmployeeRepository.existsById(nonExistentId)).thenReturn(false);
        
        // When
        boolean result = govEmployeeService.delete(nonExistentId);
        
        // Then
        assertThat(result).isFalse();
        verify(govEmployeeRepository).existsById(nonExistentId);
        verify(govEmployeeRepository, never()).deleteById(nonExistentId);
    }

    @Test
    void update_shouldReturnOptionalWithUpdatedEmployee_whenEmployeeExists() {
        // Given
        Long employeeId = 1L;
        String newName = "Updated Name";
        GovEmployee existingEmployee = new GovEmployee(employeeId, "Old Name");
        GovEmployee updatedEmployee = new GovEmployee(employeeId, newName);
        
        when(govEmployeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(govEmployeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);
        
        // When
        Optional<GovEmployee> result = govEmployeeService.update(employeeId, newName);
        
        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(newName);
        verify(govEmployeeRepository).findById(employeeId);
        verify(govEmployeeRepository).save(existingEmployee);
    }

    @Test
    void update_shouldReturnEmptyOptional_whenEmployeeDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        String newName = "Updated Name";
        when(govEmployeeRepository.findById(nonExistentId)).thenReturn(Optional.empty());
        
        // When
        Optional<GovEmployee> result = govEmployeeService.update(nonExistentId, newName);
        
        // Then
        assertThat(result).isEmpty();
        verify(govEmployeeRepository).findById(nonExistentId);
        verify(govEmployeeRepository, never()).save(any(GovEmployee.class));
    }

    @Test
    void searchByName_shouldReturnFilteredEmployees_whenNameProvided() {
        // Given
        String searchName = "Alice";
        List<GovEmployee> expectedEmployees = Arrays.asList(new GovEmployee(1L, "Alice"));
        when(govEmployeeRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(expectedEmployees);
        
        // When
        List<GovEmployee> result = govEmployeeService.searchByName(searchName);
        
        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactlyElementsOf(expectedEmployees);
        verify(govEmployeeRepository).findByNameContainingIgnoreCase(searchName);
    }

    @Test
    void searchByName_shouldReturnAllEmployees_whenNameIsNull() {
        // Given
        List<GovEmployee> allEmployees = Arrays.asList(new GovEmployee(1L, "Alice"), new GovEmployee(2L, "Bob"));
        when(govEmployeeRepository.findAll()).thenReturn(allEmployees);
        
        // When
        List<GovEmployee> result = govEmployeeService.searchByName(null);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(allEmployees);
        verify(govEmployeeRepository).findAll();
        verify(govEmployeeRepository, never()).findByNameContainingIgnoreCase(any());
    }

    @Test
    void searchByName_shouldReturnAllEmployees_whenNameIsEmpty() {
        // Given
        List<GovEmployee> allEmployees = Arrays.asList(new GovEmployee(1L, "Alice"), new GovEmployee(2L, "Bob"));
        when(govEmployeeRepository.findAll()).thenReturn(allEmployees);
        
        // When
        List<GovEmployee> result = govEmployeeService.searchByName("");
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(allEmployees);
        verify(govEmployeeRepository).findAll();
        verify(govEmployeeRepository, never()).findByNameContainingIgnoreCase(any());
    }
} 