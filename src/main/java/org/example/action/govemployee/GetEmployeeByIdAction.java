package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.GetEmployeeByIdRequest;
import org.example.action.govemployee.response.GetEmployeeByIdResponse;
import org.example.models.entity.GovEmployee;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class GetEmployeeByIdAction implements Action<GetEmployeeByIdRequest, GetEmployeeByIdResponse> {
    private final GovEmployeeService govEmployeeService;

    public GetEmployeeByIdAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public GetEmployeeByIdResponse execute(GetEmployeeByIdRequest request) {
        try {
            Optional<GovEmployee> employee = govEmployeeService.getById(request.getId());
            if (employee.isPresent()) {
                return new GetEmployeeByIdResponse(employee.get());
            } else {
                return GetEmployeeByIdResponse.notFound("Employee not found with id: " + request.getId());
            }
        } catch (Exception e) {
            return GetEmployeeByIdResponse.notFound(e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.GET_EMPLOYEE_BY_ID;
    }
} 