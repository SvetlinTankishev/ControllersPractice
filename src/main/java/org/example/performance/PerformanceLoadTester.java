package org.example.performance;

import org.example.action.animal.request.*;
import org.example.action.car.request.*;
import org.example.action.govemployee.request.*;
import org.example.action.core.ActionDispatcher;
import org.example.models.dto.AnimalDto;
import org.example.models.dto.CarDto;
import org.example.models.dto.GovEmployeeDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.Random;

/**
 * Load tester for comparing REST vs Action-based API performance.
 */
@Component
public class PerformanceLoadTester {
    private final ActionDispatcher actionDispatcher;
    private final RestTemplate restTemplate;
    private final PerformanceMonitor performanceMonitor;
    private final Random random = new Random();
    
    public PerformanceLoadTester(ActionDispatcher actionDispatcher, PerformanceMonitor performanceMonitor) {
        this.actionDispatcher = actionDispatcher;
        this.performanceMonitor = performanceMonitor;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Run a comprehensive load test comparing REST vs Action APIs.
     */
    public PerformanceTestResult runLoadTest(LoadTestConfiguration config) {
        System.out.println("🚀 Starting Performance Load Test...");
        System.out.println("Configuration: " + config);
        System.out.println("Thread Pool Size: " + (config.getConcurrentUsers() * 2) + " threads");
        System.out.println("Total Tasks: " + (config.getConcurrentUsers() * 2) + " (REST + Action pairs)");
        
        // Reset metrics before test
        performanceMonitor.reset();
        
        ExecutorService executor = Executors.newFixedThreadPool(config.getConcurrentUsers() * 2); // *2 for REST and Action
        CountDownLatch latch = new CountDownLatch(config.getConcurrentUsers() * 2); // *2 for REST and Action
        
        long testStartTime = System.currentTimeMillis();
        
        // Run REST and Action tests concurrently
        for (int i = 0; i < config.getConcurrentUsers(); i++) {
            // Submit REST test
            executor.submit(() -> {
                try {
                    runRestTests(config);
                } finally {
                    latch.countDown();
                }
            });
            
            // Submit Action test
            executor.submit(() -> {
                try {
                    runActionTests(config);
                } finally {
                    latch.countDown();
                }
            });
        }
        
        try {
            latch.await(config.getTestDurationSeconds() + 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        executor.shutdown();
        long testEndTime = System.currentTimeMillis();
        
        System.out.println("✅ Load test completed in " + (testEndTime - testStartTime) + "ms");
        
        return new PerformanceTestResult(testStartTime, testEndTime, config);
    }
    
    private void runRestTests(LoadTestConfiguration config) {
        long testStartTime = System.currentTimeMillis();
        long testEndTime = testStartTime + (config.getTestDurationSeconds() * 1000);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        System.out.println("🌐 REST test thread started at: " + (testStartTime % 100000));
        
        while (System.currentTimeMillis() < testEndTime) {
            try {
                // Test various REST endpoints
                testRestAnimals(headers);
                testRestCars(headers);
                testRestEmployees(headers);
                
                // Add small delay to simulate realistic usage
                Thread.sleep(random.nextInt(50) + 10);
            } catch (Exception e) {
                // Continue testing even if some requests fail
            }
        }
    }
    
    private void runActionTests(LoadTestConfiguration config) {
        long testStartTime = System.currentTimeMillis();
        long testEndTime = testStartTime + (config.getTestDurationSeconds() * 1000);
        
        System.out.println("⚡ Action test thread started at: " + (testStartTime % 100000));
        
        while (System.currentTimeMillis() < testEndTime) {
            try {
                // Test various Action endpoints
                testActionAnimals();
                testActionCars();
                testActionEmployees();
                
                // Add small delay to simulate realistic usage
                Thread.sleep(random.nextInt(50) + 10);
            } catch (Exception e) {
                // Continue testing even if some requests fail
            }
        }
    }
    
    private void testRestAnimals(HttpHeaders headers) {
        try {
            String baseUrl = "http://localhost:8080/api/animals";
            
            // GET all animals
            restTemplate.getForEntity(baseUrl, String.class);
            
            // POST create animal
            AnimalDto dto = new AnimalDto();
            dto.setType("TestAnimal" + random.nextInt(1000));
            HttpEntity<AnimalDto> entity = new HttpEntity<>(dto, headers);
            var response = restTemplate.postForEntity(baseUrl, entity, String.class);
            
            // GET by ID (if create was successful)
            if (response.getStatusCode().is2xxSuccessful()) {
                restTemplate.getForEntity(baseUrl + "/1", String.class);
                
                // Test UPDATE (PUT/PATCH)
                dto.setType("UpdatedAnimal" + random.nextInt(1000));
                HttpEntity<AnimalDto> updateEntity = new HttpEntity<>(dto, headers);
                restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, updateEntity, String.class);
                
                // Test HEAD request
                restTemplate.headForHeaders(baseUrl + "/1");
                
                // Test DELETE
                restTemplate.delete(baseUrl + "/1");
            }
            
            // GET search
            restTemplate.getForEntity(baseUrl + "/search?type=TestAnimal", String.class);
            
            // GET pagination
            restTemplate.getForEntity(baseUrl + "/page?page=0&size=5", String.class);
            
            // Test OPTIONS
            restTemplate.optionsForAllow(baseUrl);
            
        } catch (Exception e) {
            // Ignore errors for load testing
        }
    }
    
    private void testRestCars(HttpHeaders headers) {
        try {
            String baseUrl = "http://localhost:8080/api/cars";
            
            restTemplate.getForEntity(baseUrl, String.class);
            
            CarDto dto = new CarDto();
            dto.setBrand("TestCar" + random.nextInt(1000));
            HttpEntity<CarDto> entity = new HttpEntity<>(dto, headers);
            var response = restTemplate.postForEntity(baseUrl, entity, String.class);
            
            // Additional operations for comprehensive testing
            if (response.getStatusCode().is2xxSuccessful()) {
                restTemplate.getForEntity(baseUrl + "/1", String.class);
                dto.setBrand("UpdatedCar" + random.nextInt(1000));
                HttpEntity<CarDto> updateEntity = new HttpEntity<>(dto, headers);
                restTemplate.exchange(baseUrl + "/1", HttpMethod.PATCH, updateEntity, String.class);
                restTemplate.headForHeaders(baseUrl + "/1");
                restTemplate.delete(baseUrl + "/1");
            }
            
            restTemplate.getForEntity(baseUrl + "/search?brand=TestCar", String.class);
            restTemplate.getForEntity(baseUrl + "/page?page=0&size=5", String.class);
            restTemplate.optionsForAllow(baseUrl);
            
        } catch (Exception e) {
            // Ignore errors for load testing
        }
    }
    
    private void testRestEmployees(HttpHeaders headers) {
        try {
            String baseUrl = "http://localhost:8080/api/employees";
            
            restTemplate.getForEntity(baseUrl, String.class);
            
            GovEmployeeDto dto = new GovEmployeeDto();
            dto.setName("TestEmployee" + random.nextInt(1000));
            HttpEntity<GovEmployeeDto> entity = new HttpEntity<>(dto, headers);
            var response = restTemplate.postForEntity(baseUrl, entity, String.class);
            
            // Additional operations for comprehensive testing
            if (response.getStatusCode().is2xxSuccessful()) {
                restTemplate.getForEntity(baseUrl + "/1", String.class);
                dto.setName("UpdatedEmployee" + random.nextInt(1000));
                HttpEntity<GovEmployeeDto> updateEntity = new HttpEntity<>(dto, headers);
                restTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, updateEntity, String.class);
                restTemplate.headForHeaders(baseUrl + "/1");
                restTemplate.delete(baseUrl + "/1");
            }
            
            restTemplate.getForEntity(baseUrl + "/search?name=TestEmployee", String.class);
            restTemplate.getForEntity(baseUrl + "/page?page=0&size=5", String.class);
            restTemplate.optionsForAllow(baseUrl);
            
        } catch (Exception e) {
            // Ignore errors for load testing
        }
    }
    
    private void testActionAnimals() {
        try {
            // GET all animals
            actionDispatcher.dispatch(new GetAllAnimalsRequest());
            
            // Create animal
            actionDispatcher.dispatch(new CreateAnimalRequest("TestAnimal" + random.nextInt(1000)));
            
            // Get by ID
            actionDispatcher.dispatch(new GetAnimalByIdRequest(1L));
            
            // Update animal
            actionDispatcher.dispatch(new UpdateAnimalRequest(1L, "UpdatedAnimal" + random.nextInt(1000)));
            
            // Delete animal
            actionDispatcher.dispatch(new DeleteAnimalRequest(1L));
            
            // Search
            actionDispatcher.dispatch(new SearchAnimalsRequest("TestAnimal"));
            
            // Pagination
            actionDispatcher.dispatch(new GetAnimalsPageRequest(0, 5));
            
            // Note: HEAD and OPTIONS are HTTP-specific concepts
            // They don't have equivalent business operations in Action architecture
            
        } catch (Exception e) {
            // Ignore errors for load testing
        }
    }
    
    private void testActionCars() {
        try {
            actionDispatcher.dispatch(new GetAllCarsRequest());
            actionDispatcher.dispatch(new CreateCarRequest("TestCar" + random.nextInt(1000)));
            actionDispatcher.dispatch(new GetCarByIdRequest(1L));
            actionDispatcher.dispatch(new UpdateCarRequest(1L, "UpdatedCar" + random.nextInt(1000)));
            actionDispatcher.dispatch(new DeleteCarRequest(1L));
            actionDispatcher.dispatch(new SearchCarsRequest("TestCar"));
            actionDispatcher.dispatch(new GetCarsPageRequest(0, 5));
            
        } catch (Exception e) {
            // Ignore errors for load testing
        }
    }
    
    private void testActionEmployees() {
        try {
            actionDispatcher.dispatch(new GetAllEmployeesRequest());
            actionDispatcher.dispatch(new CreateEmployeeRequest("TestEmployee" + random.nextInt(1000)));
            actionDispatcher.dispatch(new GetEmployeeByIdRequest(1L));
            actionDispatcher.dispatch(new UpdateEmployeeRequest(1L, "UpdatedEmployee" + random.nextInt(1000)));
            actionDispatcher.dispatch(new DeleteEmployeeRequest(1L));
            actionDispatcher.dispatch(new SearchEmployeesRequest("TestEmployee"));
            actionDispatcher.dispatch(new GetEmployeesPageRequest(0, 5));
            
        } catch (Exception e) {
            // Ignore errors for load testing
        }
    }
    
    /**
     * Configuration for load testing.
     */
    public static class LoadTestConfiguration {
        private int concurrentUsers = 5;
        private int testDurationSeconds = 30;
        
        public LoadTestConfiguration() {}
        
        public LoadTestConfiguration(int concurrentUsers, int testDurationSeconds) {
            this.concurrentUsers = concurrentUsers;
            this.testDurationSeconds = testDurationSeconds;
        }
        
        // Getters and setters
        public int getConcurrentUsers() { return concurrentUsers; }
        public void setConcurrentUsers(int concurrentUsers) { this.concurrentUsers = concurrentUsers; }
        public int getTestDurationSeconds() { return testDurationSeconds; }
        public void setTestDurationSeconds(int testDurationSeconds) { this.testDurationSeconds = testDurationSeconds; }
        
        @Override
        public String toString() {
            return String.format("LoadTestConfig{users=%d, duration=%ds}", concurrentUsers, testDurationSeconds);
        }
    }
    
    /**
     * Result of a performance test.
     */
    public static class PerformanceTestResult {
        private final long startTime;
        private final long endTime;
        private final LoadTestConfiguration config;
        
        public PerformanceTestResult(long startTime, long endTime, LoadTestConfiguration config) {
            this.startTime = startTime;
            this.endTime = endTime;
            this.config = config;
        }
        
        public long getStartTime() { return startTime; }
        public long getEndTime() { return endTime; }
        public LoadTestConfiguration getConfig() { return config; }
        public long getDurationMs() { return endTime - startTime; }
    }
} 