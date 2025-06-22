package org.example.repository;

import org.example.config.IntegrationTestBase;
import org.example.models.entity.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Transactional
public class AnimalRepositoryITest extends IntegrationTestBase {
    @Autowired
    private AnimalRepository animalRepository;

    @Test
    void saveAndFindAnimal() {
        Animal animal = new Animal();
        animal.setType("cat");
        Animal saved = animalRepository.save(animal);
        assertThat(saved.getId()).isNotNull();
        Animal found = animalRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getType()).isEqualTo("cat");
    }

    @Test
    void findAllAndDelete() {
        Animal a1 = animalRepository.save(new Animal(null, "cat"));
        Animal a2 = animalRepository.save(new Animal(null, "dog"));
        List<Animal> all = animalRepository.findAll();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
        animalRepository.delete(a1);
        assertThat(animalRepository.findById(a1.getId())).isEmpty();
    }
} 