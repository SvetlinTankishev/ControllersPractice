package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.UpdateEmployeeRequest;
import org.example.action.govemployee.response.UpdateEmployeeResponse;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UpdateEmployeeAction implements Action<UpdateEmployeeRequest, UpdateEmployeeResponse> {
    private final GovEmployeeService govEmployeeService;

    public UpdateEmployeeAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public UpdateEmployeeResponse execute(UpdateEmployeeRequest request) {
        try {
            Optional<GovEmployee> updatedEmployee = govEmployeeService.update(request.getId(), request.getName());
            if (updatedEmployee.isPresent()) {
                return new UpdateEmployeeResponse(updatedEmployee.get());
            } else {
                return UpdateEmployeeResponse.notFound("Employee not found with id: " + request.getId());
            }
        } catch (Exception e) {
            return new UpdateEmployeeResponse("Failed to update employee: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.UPDATE_EMPLOYEE;
    }
} 