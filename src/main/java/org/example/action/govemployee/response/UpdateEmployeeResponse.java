package org.example.action.govemployee.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.GovEmployee;

public class UpdateEmployeeResponse extends ActionResponse {
    private final GovEmployee employee;

    public UpdateEmployeeResponse(GovEmployee employee) {
        super(true);
        this.employee = employee;
    }

    public UpdateEmployeeResponse(String message) {
        super(false, message);
        this.employee = null;
    }

    public static UpdateEmployeeResponse notFound(String message) {
        return new UpdateEmployeeResponse(message);
    }

    public GovEmployee getEmployee() {
        return employee;
    }
} 