package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.GetAllEmployeesRequest;
import org.example.action.govemployee.response.GetAllEmployeesResponse;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class GetAllEmployeesAction implements Action<GetAllEmployeesRequest, GetAllEmployeesResponse> {
    private final GovEmployeeService govEmployeeService;

    public GetAllEmployeesAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public GetAllEmployeesResponse execute(GetAllEmployeesRequest request) {
        try {
            return new GetAllEmployeesResponse(govEmployeeService.getAll());
        } catch (Exception e) {
            return new GetAllEmployeesResponse(e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.GET_ALL_EMPLOYEES;
    }
} 