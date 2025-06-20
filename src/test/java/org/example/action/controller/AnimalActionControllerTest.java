package org.example.action.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AnimalActionController.class)
public class AnimalActionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listAnimals_returnsOk() throws Exception {
        mockMvc.perform(get("/action/animal/list"))
                .andExpect(status().isOk());
    }
} 