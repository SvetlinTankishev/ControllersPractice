package org.example.action.govemployee.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.GovEmployee;

public class GetEmployeeByIdResponse extends ActionResponse {
    private final GovEmployee employee;

    public GetEmployeeByIdResponse(GovEmployee employee, boolean success, String message) {
        super(success, message);
        this.employee = employee;
    }

    public GetEmployeeByIdResponse(GovEmployee employee) {
        super(true);
        this.employee = employee;
    }

    public static GetEmployeeByIdResponse notFound(String message) {
        return new GetEmployeeByIdResponse(null, false, message);
    }

    public GovEmployee getEmployee() {
        return employee;
    }
} 