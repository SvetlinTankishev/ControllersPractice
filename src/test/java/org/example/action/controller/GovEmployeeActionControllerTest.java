package org.example.action.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GovEmployeeActionController.class)
public class GovEmployeeActionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listEmployees_returnsOk() throws Exception {
        mockMvc.perform(get("/action/gov-employee/list"))
                .andExpect(status().isOk());
    }
} 