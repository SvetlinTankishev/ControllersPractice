package org.example.action.govemployee;

/**
 * Constants for GovEmployee action types in the action-based architecture.
 */
public class GovEmployeeActionTypes {
    public static final String GET_ALL_EMPLOYEES = "GET_ALL_EMPLOYEES";
    public static final String GET_EMPLOYEE_BY_ID = "GET_EMPLOYEE_BY_ID";
    public static final String CREATE_EMPLOYEE = "CREATE_EMPLOYEE";
    public static final String UPDATE_EMPLOYEE = "UPDATE_EMPLOYEE";
    public static final String DELETE_EMPLOYEE = "DELETE_EMPLOYEE";
    public static final String SEARCH_EMPLOYEES = "SEARCH_EMPLOYEES";
    public static final String GET_EMPLOYEES_PAGE = "GET_EMPLOYEES_PAGE";
    
    private GovEmployeeActionTypes() {
        // Utility class - prevent instantiation
    }
} 