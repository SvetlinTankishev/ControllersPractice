package org.example.rest.api.animal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.rest.controller.AnimalRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.example.service.AnimalService;
import static org.mockito.Mockito.*;
import org.example.models.entity.Animal;
import java.util.Arrays;
import java.util.Collections;

@WebMvcTest(AnimalRestController.class)
public class AnimalRestControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Test
    void getAllAnimals_returnsOk() throws Exception {
        when(animalService.getAll()).thenReturn(Arrays.asList(new Animal(1L, "cat"), new Animal(2L, "dog")));
        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk());
    }

    @Test
    void patchAnimal_updatesType() throws Exception {
        Animal animal = new Animal(3L, "lion");
        when(animalService.getById(3L)).thenReturn(animal);
        when(animalService.add("lion")).thenReturn(animal);
        mockMvc.perform(post("/api/animals")
                .contentType("application/json")
                .content("{\"type\":\"lion\"}"))
                .andExpect(status().isOk());
        animal.setType("tiger");
        mockMvc.perform(patch("/api/animals/3")
                .contentType("application/json")
                .content("{\"type\":\"tiger\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("tiger"));
    }

    @Test
    void searchAnimals_byType_returnsFiltered() throws Exception {
        Animal lion = new Animal(3L, "lion");
        when(animalService.add("lion")).thenReturn(lion);
        when(animalService.searchByType("lion")).thenReturn(Collections.singletonList(lion));
        mockMvc.perform(post("/api/animals")
                .contentType("application/json")
                .content("{\"type\":\"lion\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/animals/search?type=lion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("lion"));
    }

    @Test
    void getAnimalsPage_returnsPaginated() throws Exception {
        Animal a1 = new Animal(1L, "cat");
        Animal a2 = new Animal(2L, "dog");
        Animal a3 = new Animal(3L, "lion");
        when(animalService.add("lion")).thenReturn(a3);
        when(animalService.getPage(0, 2)).thenReturn(Arrays.asList(a1, a2));
        when(animalService.getPage(1, 2)).thenReturn(Collections.singletonList(a3));
        mockMvc.perform(post("/api/animals")
                .contentType("application/json")
                .content("{\"type\":\"lion\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/animals/page?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists())
                .andExpect(jsonPath("$[1].type").exists());
        mockMvc.perform(get("/api/animals/page?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists());
    }
} 