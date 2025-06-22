package org.example.action.api.govemployee;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.action.controller.GovEmployeeActionController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.example.service.GovEmployeeService;

@WebMvcTest(GovEmployeeActionController.class)
public class GovEmployeeActionControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GovEmployeeService govEmployeeService;

    @Test
    void listEmployees_returnsOk() throws Exception {
        mockMvc.perform(get("/action/gov-employee/list"))
                .andExpect(status().isOk());
    }
} 