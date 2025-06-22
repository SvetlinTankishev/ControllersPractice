package org.example.service;

import org.example.models.entity.Animal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.example.config.IntegrationTestBase;

@SpringBootTest
@Transactional
public class AnimalServiceITest extends IntegrationTestBase {
    @Autowired
    private AnimalService animalService;

    @Test
    void addAndGetAnimal() {
        Animal animal = animalService.add("parrot");
        assertThat(animal.getId()).isNotNull();
        Animal found = animalService.getById(animal.getId());
        assertThat(found).isNotNull();
        assertThat(found.getType()).isEqualTo("parrot");
    }

    @Test
    void deleteAnimal() {
        Animal animal = animalService.add("cat");
        boolean deleted = animalService.delete(animal.getId());
        assertThat(deleted).isTrue();
        assertThat(animalService.getById(animal.getId())).isNull();
    }

    @Test
    void searchByType() {
        animalService.add("lion");
        List<Animal> lions = animalService.searchByType("lion");
        assertThat(lions).anyMatch(a -> a.getType().equals("lion"));
    }

    @Test
    void getPage() {
        animalService.add("cat");
        animalService.add("dog");
        List<Animal> page = animalService.getPage(0, 2);
        assertThat(page.size()).isLessThanOrEqualTo(2);
    }
} 