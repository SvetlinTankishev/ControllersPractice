package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class GetEmployeesPageRequest extends ActionRequest {
    private final int page;
    private final int size;

    public GetEmployeesPageRequest(int page, int size) {
        this.page = page;
        this.size = size;
    }

    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.GET_EMPLOYEES_PAGE;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
} 