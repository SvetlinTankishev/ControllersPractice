package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.CreateEmployeeRequest;
import org.example.action.govemployee.response.CreateEmployeeResponse;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class CreateEmployeeAction implements Action<CreateEmployeeRequest, CreateEmployeeResponse> {
    private final GovEmployeeService govEmployeeService;

    public CreateEmployeeAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public CreateEmployeeResponse execute(CreateEmployeeRequest request) {
        try {
            GovEmployee createdEmployee = govEmployeeService.add(request.getName());
            return new CreateEmployeeResponse(createdEmployee);
        } catch (Exception e) {
            return new CreateEmployeeResponse("Failed to create employee: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.CREATE_EMPLOYEE;
    }
} 