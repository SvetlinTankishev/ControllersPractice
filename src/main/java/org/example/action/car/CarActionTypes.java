package org.example.action.car;

/**
 * Constants defining all available Car action types.
 */
public final class CarActionTypes {
    public static final String GET_ALL_CARS = "GET_ALL_CARS";
    public static final String GET_CAR_BY_ID = "GET_CAR_BY_ID";
    public static final String CREATE_CAR = "CREATE_CAR";
    public static final String UPDATE_CAR = "UPDATE_CAR";
    public static final String DELETE_CAR = "DELETE_CAR";
    public static final String SEARCH_CARS = "SEARCH_CARS";
    public static final String GET_CARS_PAGE = "GET_CARS_PAGE";

    private CarActionTypes() {
        // Utility class
    }
} 