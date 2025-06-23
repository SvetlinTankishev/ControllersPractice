package org.example.action.animal;

/**
 * Constants defining all available Animal action types.
 */
public final class AnimalActionTypes {
    public static final String GET_ALL_ANIMALS = "GET_ALL_ANIMALS";
    public static final String GET_ANIMAL_BY_ID = "GET_ANIMAL_BY_ID";
    public static final String CREATE_ANIMAL = "CREATE_ANIMAL";
    public static final String UPDATE_ANIMAL = "UPDATE_ANIMAL";
    public static final String DELETE_ANIMAL = "DELETE_ANIMAL";
    public static final String SEARCH_ANIMALS = "SEARCH_ANIMALS";
    public static final String GET_ANIMALS_PAGE = "GET_ANIMALS_PAGE";

    private AnimalActionTypes() {
        // Utility class
    }
} 