package org.example.rest.unit;

import org.example.models.entity.Animal;
import org.example.repository.AnimalRepository;
import org.example.service.AnimalService;
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

class AnimalServiceUTest {
    @Mock
    private AnimalRepository animalRepository;
    @InjectMocks
    private AnimalService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        when(animalRepository.findAll()).thenReturn(Arrays.asList(new Animal(1L, "cat"), new Animal(2L, "dog")));
        List<Animal> animals = service.getAll();
        assertThat(animals).hasSize(2);
    }

    @Test
    void testGetById_found() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(new Animal(1L, "cat")));
        Animal animal = service.getById(1L);
        assertThat(animal).isNotNull();
        assertThat(animal.getType()).isEqualTo("cat");
    }

    @Test
    void testGetById_notFound() {
        when(animalRepository.findById(999L)).thenReturn(Optional.empty());
        Animal animal = service.getById(999L);
        assertThat(animal).isNull();
    }

    @Test
    void testAdd() {
        Animal toSave = new Animal(null, "parrot");
        Animal saved = new Animal(3L, "parrot");
        when(animalRepository.save(any(Animal.class))).thenReturn(saved);
        Animal animal = service.add("parrot");
        assertThat(animal.getType()).isEqualTo("parrot");
        assertThat(animal.getId()).isEqualTo(3L);
    }

    @Test
    void testDelete_existing() {
        when(animalRepository.existsById(1L)).thenReturn(true);
        doNothing().when(animalRepository).deleteById(1L);
        boolean removed = service.delete(1L);
        assertThat(removed).isTrue();
    }

    @Test
    void testDelete_nonExisting() {
        when(animalRepository.existsById(999L)).thenReturn(false);
        boolean removed = service.delete(999L);
        assertThat(removed).isFalse();
    }
} 