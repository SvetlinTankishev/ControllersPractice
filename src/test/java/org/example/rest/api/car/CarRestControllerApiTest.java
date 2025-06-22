package org.example.rest.api.car;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.example.rest.controller.CarRestController;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.example.service.CarService;
import static org.mockito.Mockito.*;
import org.example.models.entity.Car;
import java.util.Collections;

@WebMvcTest(CarRestController.class)
public class CarRestControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarService carService;

    @Test
    void getAllCars_returnsOk() throws Exception {
        when(carService.getAll()).thenReturn(Collections.singletonList(new Car(1L, "Toyota")));
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk());
    }

    @Test
    void searchCars_byBrand_returnsFiltered() throws Exception {
        Car honda = new Car(3L, "Honda");
        when(carService.add("Honda")).thenReturn(honda);
        when(carService.searchByBrand("Honda")).thenReturn(Collections.singletonList(honda));
        mockMvc.perform(post("/api/cars")
                .contentType("application/json")
                .content("{\"brand\":\"Honda\"}"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/cars/search?brand=Honda"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].brand").value("Honda"));
    }
} 