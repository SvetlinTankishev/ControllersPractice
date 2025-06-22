package org.example.rest.unit;

import org.example.models.entity.GovEmployee;
import org.example.repository.GovEmployeeRepository;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GovEmployeeServiceUTest {
    @Mock
    private GovEmployeeRepository govEmployeeRepository;
    @InjectMocks
    private GovEmployeeService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(govEmployeeRepository.findAll()).thenReturn(Arrays.asList(new GovEmployee(1L, "Alice"), new GovEmployee(2L, "Bob")));
        List<GovEmployee> employees = service.getAll();
        assertThat(employees).hasSize(2);
    }

    @Test
    void testGetById_found() {
        when(govEmployeeRepository.findById(1L)).thenReturn(Optional.of(new GovEmployee(1L, "Alice")));
        GovEmployee emp = service.getById(1L);
        assertThat(emp).isNotNull();
        assertThat(emp.getName()).isEqualTo("Alice");
    }

    @Test
    void testGetById_notFound() {
        when(govEmployeeRepository.findById(999L)).thenReturn(Optional.empty());
        GovEmployee emp = service.getById(999L);
        assertThat(emp).isNull();
    }

    @Test
    void testAdd() {
        GovEmployee toSave = new GovEmployee(null, "Charlie");
        GovEmployee saved = new GovEmployee(3L, "Charlie");
        when(govEmployeeRepository.save(any(GovEmployee.class))).thenReturn(saved);
        GovEmployee emp = service.add("Charlie");
        assertThat(emp.getName()).isEqualTo("Charlie");
        assertThat(emp.getId()).isEqualTo(3L);
    }

    @Test
    void testDelete_existing() {
        when(govEmployeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(govEmployeeRepository).deleteById(1L);
        boolean removed = service.delete(1L);
        assertThat(removed).isTrue();
    }

    @Test
    void testDelete_nonExisting() {
        when(govEmployeeRepository.existsById(999L)).thenReturn(false);
        boolean removed = service.delete(999L);
        assertThat(removed).isFalse();
    }
} 