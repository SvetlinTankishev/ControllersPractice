package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.DeleteEmployeeRequest;
import org.example.action.govemployee.response.DeleteEmployeeResponse;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class DeleteEmployeeAction implements Action<DeleteEmployeeRequest, DeleteEmployeeResponse> {
    private final GovEmployeeService govEmployeeService;

    public DeleteEmployeeAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public DeleteEmployeeResponse execute(DeleteEmployeeRequest request) {
        try {
            boolean deleted = govEmployeeService.delete(request.getId());
            if (deleted) {
                return DeleteEmployeeResponse.success();
            } else {
                return DeleteEmployeeResponse.notFound("Employee not found with id: " + request.getId());
            }
        } catch (Exception e) {
            return DeleteEmployeeResponse.notFound("Failed to delete employee: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.DELETE_EMPLOYEE;
    }
} 