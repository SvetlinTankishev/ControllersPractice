package org.example.action.govemployee.request;

import org.example.action.core.ActionRequest;
import org.example.action.govemployee.GovEmployeeActionTypes;

public class GetAllEmployeesRequest extends ActionRequest {
    @Override
    public String getActionType() {
        return GovEmployeeActionTypes.GET_ALL_EMPLOYEES;
    }
} 