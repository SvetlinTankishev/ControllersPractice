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
} 