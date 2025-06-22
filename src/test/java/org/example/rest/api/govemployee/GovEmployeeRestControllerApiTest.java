package org.example.rest.api.govemployee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.rest.controller.GovEmployeeRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.example.service.GovEmployeeService;
import static org.mockito.Mockito.*;
import org.example.models.entity.GovEmployee;
import java.util.Arrays;
import java.util.Collections;

@WebMvcTest(GovEmployeeRestController.class)
public class GovEmployeeRestControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GovEmployeeService govEmployeeService;

    @Test
    void getAllEmployees_returnsOk() throws Exception {
        when(govEmployeeService.getAll()).thenReturn(Arrays.asList(new GovEmployee(1L, "Alice"), new GovEmployee(2L, "Bob")));
        mockMvc.perform(get("/api/gov-employees"))
                .andExpect(status().isOk());
    }

    @Test
    void searchEmployees_byName_returnsFiltered() throws Exception {
        GovEmployee charlie = new GovEmployee(3L, "Charlie");
        when(govEmployeeService.add("Charlie")).thenReturn(charlie);
        when(govEmployeeService.searchByName("Charlie")).thenReturn(Collections.singletonList(charlie));
        mockMvc.perform(post("/api/gov-employees")
                .contentType("application/json")
                .content("{\"name\":\"Charlie\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/gov-employees/search?name=Charlie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Charlie"));
    }

    @Test
    void getEmployeesPage_returnsPaginated() throws Exception {
        GovEmployee e1 = new GovEmployee(1L, "Alice");
        GovEmployee e2 = new GovEmployee(2L, "Bob");
        GovEmployee e3 = new GovEmployee(3L, "Charlie");
        when(govEmployeeService.add("Charlie")).thenReturn(e3);
        when(govEmployeeService.getPage(0, 2)).thenReturn(Arrays.asList(e1, e2));
        when(govEmployeeService.getPage(1, 2)).thenReturn(Collections.singletonList(e3));
        mockMvc.perform(post("/api/gov-employees")
                .contentType("application/json")
                .content("{\"name\":\"Charlie\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/gov-employees/page?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
        mockMvc.perform(get("/api/gov-employees/page?page=1&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }
} 