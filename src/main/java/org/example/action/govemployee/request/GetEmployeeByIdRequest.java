package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class GetEmployeeByIdRequest extends ActionRequest {
    private final Long id;

    public GetEmployeeByIdRequest(Long id) {
        this.id = id;
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.GET_EMPLOYEE_BY_ID;
    }

    public Long getId() {
        return id;
    }
} 