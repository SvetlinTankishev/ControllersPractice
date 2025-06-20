package org.example.rest.dto;

public class CarDto {
    private String brand;
    public CarDto() {}
    public CarDto(String brand) { this.brand = brand; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
} 