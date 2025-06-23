package org.example.rest.unit;

import org.example.models.entity.GovEmployee;
import org.example.repository.GovEmployeeRepository;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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

    @ParameterizedTest(name = "Get by ID: {0}")
    @MethodSource("getByIdData")
    void getById_shouldHandleVariousScenarios(String testName, Long employeeId, Optional<GovEmployee> repositoryResult, boolean shouldBePresent, String expectedName) {
        // Given
        when(govEmployeeRepository.findById(employeeId)).thenReturn(repositoryResult);
        
        // When
        Optional<GovEmployee> result = govEmployeeService.getById(employeeId);
        
        // Then
        assertThat(result.isPresent()).isEqualTo(shouldBePresent);
        if (shouldBePresent) {
            assertThat(result.get().getName()).isEqualTo(expectedName);
        }
        verify(govEmployeeRepository).findById(employeeId);
    }

    static Stream<Arguments> getByIdData() {
        return Stream.of(
                Arguments.of("Employee exists", 1L, Optional.of(new GovEmployee(1L, "Alice")), true, "Alice"),
                Arguments.of("Employee does not exist", 999L, Optional.empty(), false, null)
        );
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

    @ParameterizedTest(name = "Delete employee: {0}")
    @MethodSource("deleteData")
    void delete_shouldHandleVariousScenarios(String testName, Long employeeId, boolean existsInRepo, boolean expectedResult, boolean shouldCallDelete) {
        // Given
        when(govEmployeeRepository.existsById(employeeId)).thenReturn(existsInRepo);
        if (shouldCallDelete) {
            doNothing().when(govEmployeeRepository).deleteById(employeeId);
        }
        
        // When
        boolean result = govEmployeeService.delete(employeeId);
        
        // Then
        assertThat(result).isEqualTo(expectedResult);
        verify(govEmployeeRepository).existsById(employeeId);
        if (shouldCallDelete) {
            verify(govEmployeeRepository).deleteById(employeeId);
        } else {
            verify(govEmployeeRepository, never()).deleteById(employeeId);
        }
    }

    static Stream<Arguments> deleteData() {
        return Stream.of(
                Arguments.of("Employee exists", 1L, true, true, true),
                Arguments.of("Employee does not exist", 999L, false, false, false)
        );
    }

    @ParameterizedTest(name = "Update employee: {0}")
    @MethodSource("updateData")
    void update_shouldHandleVariousScenarios(String testName, Long employeeId, Optional<GovEmployee> repositoryResult, String newName, boolean shouldBePresent, boolean shouldCallSave) {
        // Given
        when(govEmployeeRepository.findById(employeeId)).thenReturn(repositoryResult);
        if (shouldCallSave && repositoryResult.isPresent()) {
            GovEmployee updatedEmployee = new GovEmployee(employeeId, newName);
            when(govEmployeeRepository.save(repositoryResult.get())).thenReturn(updatedEmployee);
        }
        
        // When
        Optional<GovEmployee> result = govEmployeeService.update(employeeId, newName);
        
        // Then
        assertThat(result.isPresent()).isEqualTo(shouldBePresent);
        if (shouldBePresent) {
            assertThat(result.get().getName()).isEqualTo(newName);
        }
        verify(govEmployeeRepository).findById(employeeId);
        if (shouldCallSave) {
            verify(govEmployeeRepository).save(any(GovEmployee.class));
        } else {
            verify(govEmployeeRepository, never()).save(any(GovEmployee.class));
        }
    }

    static Stream<Arguments> updateData() {
        return Stream.of(
                Arguments.of("Employee exists", 1L, Optional.of(new GovEmployee(1L, "Old Name")), "Updated Name", true, true),
                Arguments.of("Employee does not exist", 999L, Optional.empty(), "Updated Name", false, false)
        );
    }

    @ParameterizedTest(name = "Search by name: {0}")
    @MethodSource("searchByNameData")
    void searchByName_shouldHandleVariousScenarios(String testName, String searchName, List<GovEmployee> expectedEmployees, boolean shouldCallSpecificSearch) {
        // Given
        if (shouldCallSpecificSearch) {
            when(govEmployeeRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(expectedEmployees);
        } else {
            when(govEmployeeRepository.findAll()).thenReturn(expectedEmployees);
        }
        
        // When
        List<GovEmployee> result = govEmployeeService.searchByName(searchName);
        
        // Then
        assertThat(result).hasSize(expectedEmployees.size());
        assertThat(result).containsExactlyElementsOf(expectedEmployees);
        
        if (shouldCallSpecificSearch) {
            verify(govEmployeeRepository).findByNameContainingIgnoreCase(searchName);
            verify(govEmployeeRepository, never()).findAll();
        } else {
            verify(govEmployeeRepository).findAll();
            verify(govEmployeeRepository, never()).findByNameContainingIgnoreCase(any());
        }
    }

    static Stream<Arguments> searchByNameData() {
        List<GovEmployee> aliceEmployees = Arrays.asList(new GovEmployee(1L, "Alice"));
        List<GovEmployee> allEmployees = Arrays.asList(new GovEmployee(1L, "Alice"), new GovEmployee(2L, "Bob"));
        List<GovEmployee> emptyEmployees = Arrays.asList(); // No employees match "   "
        
        return Stream.of(
                Arguments.of("Specific name provided", "Alice", aliceEmployees, true),
                Arguments.of("Name is null", null, allEmployees, false),
                Arguments.of("Name is empty", "", allEmployees, false),
                Arguments.of("Name is blank spaces", "   ", emptyEmployees, true)
        );
    }

    @Test
    void getPage_shouldReturnPagedResults_whenValidParametersProvided() {
        // Given
        int page = 0;
        int size = 5;
        List<GovEmployee> expectedEmployees = Arrays.asList(new GovEmployee(1L, "Alice"), new GovEmployee(2L, "Bob"));
        org.springframework.data.domain.PageRequest pageRequest = org.springframework.data.domain.PageRequest.of(page, size);
        org.springframework.data.domain.Page<GovEmployee> mockPage = mock(org.springframework.data.domain.Page.class);
        when(govEmployeeRepository.findAll(pageRequest)).thenReturn(mockPage);
        when(mockPage.getContent()).thenReturn(expectedEmployees);
        
        // When
        List<GovEmployee> result = govEmployeeService.getPage(page, size);
        
        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(expectedEmployees);
        verify(govEmployeeRepository).findAll(pageRequest);
        verify(mockPage).getContent();
    }
} 