package org.example.action.govemployee;

import org.example.action.core.Action;
import org.example.action.govemployee.request.SearchEmployeesRequest;
import org.example.action.govemployee.response.SearchEmployeesResponse;
import org.example.service.GovEmployeeService;
import org.springframework.stereotype.Component;

@Component
public class SearchEmployeesAction implements Action<SearchEmployeesRequest, SearchEmployeesResponse> {
    private final GovEmployeeService govEmployeeService;

    public SearchEmployeesAction(GovEmployeeService govEmployeeService) {
        this.govEmployeeService = govEmployeeService;
    }

    @Override
    public SearchEmployeesResponse execute(SearchEmployeesRequest request) {
        try {
            return new SearchEmployeesResponse(govEmployeeService.searchByName(request.getName()));
        } catch (Exception e) {
            return new SearchEmployeesResponse("Failed to search employees: " + e.getMessage());
        }
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.SEARCH_EMPLOYEES;
    }
} 