package org.example.action.controller;

import org.example.action.govemployee.request.*;
import org.example.action.govemployee.response.*;
import org.example.action.core.ActionDispatcher;
import org.example.models.dto.GovEmployeeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controller that exposes action-based endpoints for GovEmployee operations.
 */
@RestController
@RequestMapping("/actions/employees")
public class GovEmployeeActionController {
    private final ActionDispatcher actionDispatcher;

    public GovEmployeeActionController(ActionDispatcher actionDispatcher) {
        this.actionDispatcher = actionDispatcher;
    }

    @PostMapping("/get-all")
    public ResponseEntity<GetAllEmployeesResponse> getAllEmployees() {
        GetAllEmployeesRequest request = new GetAllEmployeesRequest();
        GetAllEmployeesResponse response = actionDispatcher.dispatch(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<GetEmployeeByIdResponse> getEmployeeById(@RequestBody GetEmployeeByIdRequest request) {
        GetEmployeeByIdResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<CreateEmployeeResponse> createEmployee(@Valid @RequestBody GovEmployeeDto dto) {
        CreateEmployeeRequest request = new CreateEmployeeRequest(dto.getName());
        CreateEmployeeResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateEmployeeResponse> updateEmployee(@RequestBody UpdateEmployeeRequest request) {
        UpdateEmployeeResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteEmployeeResponse> deleteEmployee(@RequestBody DeleteEmployeeRequest request) {
        DeleteEmployeeResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/search")
    public ResponseEntity<SearchEmployeesResponse> searchEmployees(@RequestBody SearchEmployeesRequest request) {
        SearchEmployeesResponse response = actionDispatcher.dispatch(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-page")
    public ResponseEntity<GetEmployeesPageResponse> getEmployeesPage(@RequestBody GetEmployeesPageRequest request) {
        GetEmployeesPageResponse response = actionDispatcher.dispatch(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
} 