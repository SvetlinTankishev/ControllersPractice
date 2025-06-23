package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class SearchEmployeesRequest extends ActionRequest {
    private final String name;

    public SearchEmployeesRequest(String name) {
        this.name = name;
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.SEARCH_EMPLOYEES;
    }

    public String getName() {
        return name;
    }
} 