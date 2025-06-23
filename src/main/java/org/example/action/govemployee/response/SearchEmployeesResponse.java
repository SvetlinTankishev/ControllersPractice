package org.example.action.govemployee.response;

import org.example.action.core.ActionResponse;
import org.example.models.entity.GovEmployee;
import java.util.List;

public class SearchEmployeesResponse extends ActionResponse {
    private final List<GovEmployee> employees;

    public SearchEmployeesResponse(List<GovEmployee> employees) {
        super(true);
        this.employees = employees;
    }

    public SearchEmployeesResponse(String message) {
        super(false, message);
        this.employees = null;
    }

    public List<GovEmployee> getEmployees() {
        return employees;
    }
} 