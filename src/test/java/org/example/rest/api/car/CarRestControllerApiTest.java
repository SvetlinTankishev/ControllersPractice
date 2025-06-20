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
} 