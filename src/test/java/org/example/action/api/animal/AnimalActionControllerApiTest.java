package org.example.action.api.animal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.action.controller.AnimalActionController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.example.service.AnimalService;

@WebMvcTest(AnimalActionController.class)
public class AnimalActionControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnimalService animalService;

    @Test
    void listAnimals_returnsOk() throws Exception {
        mockMvc.perform(get("/action/animal/list"))
                .andExpect(status().isOk());
    }
} 