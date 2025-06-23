package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.action.core.ActionDispatcher;
import org.example.repository.AnimalRepository;
import org.example.repository.CarRepository;
import org.example.repository.GovEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
public abstract class ApiTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
    
    @Autowired
    protected ActionDispatcher actionDispatcher;

    @Autowired
    protected AnimalRepository animalRepository;

    @Autowired
    protected CarRepository carRepository;

    @Autowired
    protected GovEmployeeRepository govEmployeeRepository;

    /**
     * Validates that a request returns a successful response (2xx status)
     */
    public ResultActions validateSuccess(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    /**
     * Validates that a request returns a 201 Created response
     */
    public ResultActions validateCreated(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isCreated());
    }

    /**
     * Validates that a request returns a 204 No Content response
     */
    public ResultActions validateNoContent(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    /**
     * Validates that a request returns a 404 Not Found response
     */
    public ResultActions validateNotFound(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    /**
     * Validates that a request returns a 400 Bad Request response
     */
    public ResultActions validateBadRequest(MockHttpServletRequestBuilder request) throws Exception {
        return mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    /**
     * Creates a JSON content string from an object
     */
    protected String toJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
    }

    /**
     * Helper method to create a POST request with JSON content
     */
    protected MockHttpServletRequestBuilder postJson(String url, Object content) throws Exception {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content));
    }

    /**
     * Helper method to create a POST request with no content for action endpoints
     */
    protected MockHttpServletRequestBuilder postJson(String url) throws Exception {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Helper method to create a PATCH request with JSON content
     */
    protected MockHttpServletRequestBuilder patchJson(String url, Object content) throws Exception {
        return MockMvcRequestBuilders.patch(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(content));
    }

    /**
     * Helper method to create a GET request
     */
    protected MockHttpServletRequestBuilder getJson(String url) {
        return MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Helper method to create a DELETE request
     */
    protected MockHttpServletRequestBuilder deleteJson(String url) {
        return MockMvcRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Validates that a response contains an entity with the expected ID
     */
    protected ResultActions expectEntityWithId(ResultActions resultActions, Long expectedId) throws Exception {
        return resultActions.andExpect(jsonPath("$.id").value(expectedId));
    }

    /**
     * Validates that a response contains a list with the expected size
     */
    protected ResultActions expectListSize(ResultActions resultActions, int expectedSize) throws Exception {
        return resultActions.andExpect(jsonPath("$.length()").value(expectedSize));
    }

    /**
     * Validates that a response contains a field with the expected value
     */
    protected ResultActions expectFieldValue(ResultActions resultActions, String fieldPath, Object expectedValue) throws Exception {
        return resultActions.andExpect(jsonPath(fieldPath).value(expectedValue));
    }

    /**
     * Validates that a response contains a field that exists
     */
    protected ResultActions expectFieldExists(ResultActions resultActions, String fieldPath) throws Exception {
        return resultActions.andExpect(jsonPath(fieldPath).exists());
    }

    /**
     * Validates that a response contains a field that doesn't exist
     */
    protected ResultActions expectFieldDoesNotExist(ResultActions resultActions, String fieldPath) throws Exception {
        return resultActions.andExpect(jsonPath(fieldPath).doesNotExist());
    }

    /**
     * Validates that an action response contains success=true
     */
    protected ResultActions expectActionSuccess(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(jsonPath("$.success").value(true));
    }

    /**
     * Validates that an action response contains success=false
     */
    protected ResultActions expectActionFailure(ResultActions resultActions) throws Exception {
        return resultActions.andExpect(jsonPath("$.success").value(false));
    }

    /**
     * Validates that an action response contains a specific message
     */
    protected ResultActions expectActionMessage(ResultActions resultActions, String expectedMessage) throws Exception {
        return resultActions.andExpect(jsonPath("$.message").value(expectedMessage));
    }

    /**
     * Cleans up all test data from the database
     */
    protected void cleanupTestData() {
        animalRepository.deleteAll();
        carRepository.deleteAll();
        govEmployeeRepository.deleteAll();
    }

    /**
     * Creates a test animal and returns its ID
     */
    protected Long createTestAnimal(String type) {
        var animal = new org.example.models.entity.Animal();
        animal.setType(type);
        return animalRepository.save(animal).getId();
    }

    /**
     * Creates a test car and returns its ID
     */
    protected Long createTestCar(String brand) {
        var car = new org.example.models.entity.Car();
        car.setBrand(brand);
        return carRepository.save(car).getId();
    }

    /**
     * Creates a test employee and returns its ID
     */
    protected Long createTestEmployee(String name) {
        var employee = new org.example.models.entity.GovEmployee();
        employee.setName(name);
        return govEmployeeRepository.save(employee).getId();
    }
} 