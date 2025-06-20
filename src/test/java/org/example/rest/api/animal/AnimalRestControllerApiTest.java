package org.example.rest.api.animal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.rest.controller.AnimalRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalRestController.class)
public class AnimalRestControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllAnimals_returnsOk() throws Exception {
        mockMvc.perform(get("/api/animals"))
                .andExpect(status().isOk());
    }

    @Test
    void patchAnimal_updatesType() throws Exception {
        // Add a new animal first
        mockMvc.perform(post("/api/animals")
                .contentType("application/json")
                .content("{\"type\":\"lion\"}"))
                .andExpect(status().isOk());

        // Patch the animal with id 3 (since 1 and 2 are created by default)
        mockMvc.perform(patch("/api/animals/3")
                .contentType("application/json")
                .content("{\"type\":\"tiger\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("tiger"));
    }

    @Test
    void searchAnimals_byType_returnsFiltered() throws Exception {
        // Add a new animal
        mockMvc.perform(post("/api/animals")
                .contentType("application/json")
                .content("{\"type\":\"lion\"}"))
                .andExpect(status().isOk());

        // Search for 'lion'
        mockMvc.perform(get("/api/animals/search?type=lion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("lion"));
    }

    @Test
    void getAnimalsPage_returnsPaginated() throws Exception {
        // Add a new animal to ensure more than 2 exist
        mockMvc.perform(post("/api/animals")
                .contentType("application/json")
                .content("{\"type\":\"lion\"}"))
                .andExpect(status().isOk());

        // Page 0, size 2
        mockMvc.perform(get("/api/animals/page?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists())
                .andExpect(jsonPath("$[1].type").exists());

        // Page 1, size 2 (should contain at least 1 animal if 3 exist)
        mockMvc.perform(get("/api/animals/page?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").exists());
    }
} 