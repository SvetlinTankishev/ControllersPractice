package org.example.rest.api.car;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.rest.controller.CarRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarRestController.class)
public class CarRestControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCars_returnsOk() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk());
    }

    @Test
    void searchCars_byBrand_returnsFiltered() throws Exception {
        // Add a new car
        mockMvc.perform(post("/api/cars")
                .contentType("application/json")
                .content("{\"brand\":\"Honda\"}"))
                .andExpect(status().isOk());

        // Search for 'Honda'
        mockMvc.perform(get("/api/cars/search?brand=Honda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Honda"));
    }
} 