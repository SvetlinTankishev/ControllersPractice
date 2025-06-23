package org.example.models.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
public class CarDto {
    @NotBlank(message = "Brand cannot be blank")
    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    private String brand;
} 