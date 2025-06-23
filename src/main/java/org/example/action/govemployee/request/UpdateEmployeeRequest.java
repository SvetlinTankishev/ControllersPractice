package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class UpdateEmployeeRequest extends ActionRequest {
    private final Long id;
    private final String name;

    public UpdateEmployeeRequest(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.UPDATE_EMPLOYEE;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
} 