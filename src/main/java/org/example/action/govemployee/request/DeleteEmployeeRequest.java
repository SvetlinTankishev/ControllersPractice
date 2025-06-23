package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class DeleteEmployeeRequest extends ActionRequest {
    private final Long id;

    public DeleteEmployeeRequest(Long id) {
        this.id = id;
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.DELETE_EMPLOYEE;
    }

    public Long getId() {
        return id;
    }
} 