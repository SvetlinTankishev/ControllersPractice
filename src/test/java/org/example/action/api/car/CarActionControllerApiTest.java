package org.example.action.api.car;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.action.controller.CarActionController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarActionController.class)
public class CarActionControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listCars_returnsOk() throws Exception {
        mockMvc.perform(get("/action/car/list"))
                .andExpect(status().isOk());
    }
} 