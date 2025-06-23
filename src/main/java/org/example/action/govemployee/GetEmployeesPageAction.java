package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.GetEmployeesPageRequest;
import org.example.action.govemployee.response.GetEmployeesPageResponse;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class GetEmployeesPageAction implements Action<GetEmployeesPageRequest, GetEmployeesPageResponse> {
    private final GovEmployeeService govEmployeeService;

    public GetEmployeesPageAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public GetEmployeesPageResponse execute(GetEmployeesPageRequest request) {
        try {
            if (request.getPage() < 0 || request.getSize() <= 0) {
                return new GetEmployeesPageResponse("Page must be >= 0 and size must be > 0");
            }
            return new GetEmployeesPageResponse(govEmployeeService.getPage(request.getPage(), request.getSize()));
        } catch (Exception e) {
            return new GetEmployeesPageResponse("Failed to get employees page: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.GET_EMPLOYEES_PAGE;
    }
} 