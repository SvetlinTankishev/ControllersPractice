# Action-Based Architecture Implementation

## Overview

This project now showcases **both RESTful and Action-Based architectural patterns** side by side, demonstrating how different architectural approaches can coexist in the same application while sharing common business logic layers.

## Architecture Comparison

### RESTful Pattern
- **Endpoint Structure**: `/api/{resource}` (e.g., `/api/cars`, `/api/animals`)
- **HTTP Methods**: GET, POST, PATCH, DELETE
- **Resource-Oriented**: Operations are mapped to HTTP verbs on resources
- **Location**: `src/main/java/org/example/rest/`

### Action-Based Pattern
- **Endpoint Structure**: `/actions/{resource}/{action}` (e.g., `/actions/cars/get-all`)
- **HTTP Methods**: Primarily POST with action intent in URL
- **Command-Oriented**: Each endpoint represents a specific business action
- **Location**: `src/main/java/org/example/action/`

## Action-Based Architecture Structure

```
src/main/java/org/example/action/
├── core/                           # Core action infrastructure
│   ├── Action.java                 # Base action interface
│   ├── ActionRequest.java          # Base request class
│   ├── ActionResponse.java         # Base response class
│   └── ActionDispatcher.java       # Central dispatcher
├── car/                           # Car-specific actions
│   ├── CarActionTypes.java        # Action type constants
│   ├── request/                   # Request models
│   │   ├── GetAllCarsRequest.java
│   │   ├── GetCarByIdRequest.java
│   │   ├── CreateCarRequest.java
│   │   ├── UpdateCarRequest.java
│   │   ├── DeleteCarRequest.java
│   │   ├── SearchCarsRequest.java
│   │   └── GetCarsPageRequest.java
│   ├── response/                  # Response models
│   │   ├── GetAllCarsResponse.java
│   │   └── ... (corresponding responses)
│   ├── GetAllCarsAction.java      # Action implementations
│   ├── GetCarByIdAction.java
│   ├── CreateCarAction.java
│   ├── UpdateCarAction.java
│   ├── DeleteCarAction.java
│   ├── SearchCarsAction.java
│   └── GetCarsPageAction.java
├── animal/                        # Animal-specific actions (similar structure)
├── controller/                    # Action controllers
│   ├── CarActionController.java
│   └── AnimalActionController.java
```

## Key Components

### 1. Action Interface
```java
public interface Action<TRequest, TResponse> {
    TResponse execute(TRequest request);
    String getActionType();
}
```

### 2. ActionDispatcher
- **Purpose**: Central routing mechanism for action requests
- **Functionality**: Maps action types to their corresponding handlers
- **Benefits**: Decouples controllers from specific action implementations

### 3. Request/Response Models
- **Strongly Typed**: Each action has specific request and response models
- **Validation**: Built-in validation support
- **Consistency**: Standardized success/failure handling

### 4. Action Implementations
- **Single Responsibility**: Each action class handles one specific operation
- **Service Integration**: Reuses existing service layer (Car/Animal/GovEmployee services)
- **Error Handling**: Consistent error handling across all actions

## Endpoint Examples

### RESTful Endpoints
```
GET    /api/cars           # Get all cars
GET    /api/cars/1         # Get car by ID
POST   /api/cars           # Create car
PATCH  /api/cars/1         # Update car
DELETE /api/cars/1         # Delete car
GET    /api/cars/search?brand=BMW # Search cars
GET    /api/cars/page?page=0&size=10 # Paginated cars
```

### Action-Based Endpoints
```
POST /actions/cars/get-all         # Get all cars
POST /actions/cars/get-by-id       # Get car by ID (ID in request body)
POST /actions/cars/create          # Create car
POST /actions/cars/update          # Update car (ID and data in request body)
POST /actions/cars/delete          # Delete car (ID in request body)
POST /actions/cars/search          # Search cars (criteria in request body)
POST /actions/cars/get-page        # Paginated cars (page info in request body)
```

## Request/Response Examples

### Action Request Example
```json
// POST /actions/cars/get-by-id
{
    "id": 1
}
```

### Action Response Example
```json
{
    "success": true,
    "message": null,
    "car": {
        "id": 1,
        "brand": "Toyota",
        "createdAt": "2025-01-01T12:00:00",
        "updatedAt": "2025-01-01T12:00:00"
    }
}
```

### Error Response Example
```json
{
    "success": false,
    "message": "Car not found with id: 999",
    "car": null
}
```

## Shared Architecture

Both patterns share the same:
- **Service Layer**: `CarService`, `AnimalService`, `GovEmployeeService`
- **Repository Layer**: `CarRepository`, `AnimalRepository`, `GovEmployeeRepository`
- **Domain Models**: `Car`, `Animal`, `GovEmployee` entities
- **Database Schema**: Same tables and migrations
- **Configuration**: JPA, database connections, etc.

## Benefits of Action-Based Pattern

### 1. **Explicit Intent**
- Each endpoint clearly states what action is being performed
- No ambiguity about HTTP verb semantics

### 2. **Consistent Interface**
- All operations use POST with clear action names
- Uniform request/response structure

### 3. **Flexible Parameters**
- Complex operations can be easily parameterized
- No URL length limitations

### 4. **Type Safety**
- Strongly typed request/response models
- Compile-time validation of action signatures

### 5. **Centralized Dispatching**
- Single point of entry for all actions
- Easy to add cross-cutting concerns (logging, metrics, security)

### 6. **Testability**
- Each action is independently testable
- Clear separation of concerns

## Testing Strategy

### Unit Tests
- **Location**: `src/test/java/org/example/action/unit/`
- **Focus**: Individual action classes
- **Mocking**: Service layer dependencies
- **Examples**: `GetAllCarsActionUTest`, `CreateCarActionUTest`

### Integration Tests
- **Location**: `src/test/java/org/example/action/integration/`
- **Focus**: ActionDispatcher and end-to-end action flow
- **Database**: Uses test containers for real database integration
- **Example**: `ActionDispatcherITest`

### API Tests
- **Location**: `src/test/java/org/example/action/api/`
- **Focus**: HTTP endpoints and request/response handling
- **Framework**: MockMvc for HTTP testing
- **Example**: `CarActionControllerApiTest`

### Test Infrastructure
- **ActionTestBase**: Common test utilities for action testing
- **Helper Methods**: Standardized test patterns
- **Data Setup**: Consistent test data creation

## When to Use Each Pattern

### Use RESTful When:
- Working with standard CRUD operations
- Following REST conventions is important
- Integration with existing REST clients
- Resource-centric thinking fits the domain

### Use Action-Based When:
- Complex business operations that don't map well to HTTP verbs
- Need for explicit operation naming
- Command-oriented workflows
- Complex parameter passing requirements
- Building RPC-style APIs

## Future Enhancements

1. **Authentication/Authorization**: Add security layers to action dispatcher
2. **Caching**: Implement caching strategies for read actions
3. **Metrics**: Add performance monitoring and metrics collection
4. **Validation**: Enhanced request validation framework
5. **Documentation**: Auto-generate API documentation from action definitions
6. **Async Actions**: Support for long-running operations

## Conclusion

This implementation demonstrates how both RESTful and Action-Based patterns can coexist effectively in the same application. Each pattern has its strengths, and the choice between them can be made based on specific use cases and requirements. The shared service layer ensures consistency while allowing for architectural flexibility at the presentation layer. 