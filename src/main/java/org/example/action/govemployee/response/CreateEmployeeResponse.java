package org.example.action.govemployee.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.GovEmployee;

public class CreateEmployeeResponse extends ActionResponse {
    private final GovEmployee employee;

    public CreateEmployeeResponse(GovEmployee employee) {
        super(true);
        this.employee = employee;
    }

    public CreateEmployeeResponse(String message) {
        super(false, message);
        this.employee = null;
    }

    public GovEmployee getEmployee() {
        return employee;
    }
} 