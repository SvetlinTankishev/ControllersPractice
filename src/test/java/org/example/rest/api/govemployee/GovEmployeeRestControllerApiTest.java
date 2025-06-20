package org.example.rest.api.govemployee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.rest.controller.GovEmployeeRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GovEmployeeRestController.class)
public class GovEmployeeRestControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllEmployees_returnsOk() throws Exception {
        mockMvc.perform(get("/api/gov-employees"))
                .andExpect(status().isOk());
    }
} 