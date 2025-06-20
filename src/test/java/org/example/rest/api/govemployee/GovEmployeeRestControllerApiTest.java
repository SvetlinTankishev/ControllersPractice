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

    @Test
    void searchEmployees_byName_returnsFiltered() throws Exception {
        // Add a new employee
        mockMvc.perform(post("/api/gov-employees")
                .contentType("application/json")
                .content("{\"name\":\"Charlie\"}"))
                .andExpect(status().isOk());

        // Search for 'Charlie'
        mockMvc.perform(get("/api/gov-employees/search?name=Charlie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Charlie"));
    }

    @Test
    void getEmployeesPage_returnsPaginated() throws Exception {
        // Add a new employee to ensure more than 2 exist
        mockMvc.perform(post("/api/gov-employees")
                .contentType("application/json")
                .content("{\"name\":\"Charlie\"}"))
                .andExpect(status().isOk());

        // Page 0, size 2
        mockMvc.perform(get("/api/gov-employees/page?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());

        // Page 1, size 2 (should contain at least 1 employee if 3 exist)
        mockMvc.perform(get("/api/gov-employees/page?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }
} 