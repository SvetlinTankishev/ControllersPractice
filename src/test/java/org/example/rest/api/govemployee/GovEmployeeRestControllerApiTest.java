package org.example.rest.api.govemployee;

import org.example.config.ApiTestBase;
import org.example.service.GovEmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class GovEmployeeRestControllerApiTest extends ApiTestBase {

    @Autowired
    private GovEmployeeService govEmployeeService;

    @Test
    void getAllEmployees_returnsOk() throws Exception {
        govEmployeeService.add("Alice");
        validateSuccess(get("/api/employees"));
    }

    @Test
    void searchEmployees_byName_returnsFiltered() throws Exception {
        govEmployeeService.add("Charlie");
        validateSuccess(get("/api/employees/search?name=Charlie"))
                .andExpect(jsonPath("$[0].name").value("Charlie"));
    }

    @Test
    void getEmployeesPage_returnsPaginated() throws Exception {
        govEmployeeService.add("Alice");
        govEmployeeService.add("Bob");
        validateSuccess(get("/api/employees/page?page=0&size=2"))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[1].name").exists());
    }
} 