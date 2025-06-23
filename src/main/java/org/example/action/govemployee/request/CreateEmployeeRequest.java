package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class CreateEmployeeRequest extends ActionRequest {
    private final String name;

    public CreateEmployeeRequest(String name) {
        this.name = name;
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.CREATE_EMPLOYEE;
    }

    public String getName() {
        return name;
    }
} 