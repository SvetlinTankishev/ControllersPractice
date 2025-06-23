package org.example.action.govemployee.response;

import org.example.action.core.ActionResponse;

public class DeleteEmployeeResponse extends ActionResponse {

    public DeleteEmployeeResponse(boolean success, String message) {
        super(success, message);
    }

    public DeleteEmployeeResponse(boolean success) {
        super(success);
    }

    public static DeleteEmployeeResponse success() {
        return new DeleteEmployeeResponse(true, "Employee deleted successfully");
    }

    public static DeleteEmployeeResponse notFound(String message) {
        return new DeleteEmployeeResponse(false, message);
    }
} 