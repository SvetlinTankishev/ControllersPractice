package org.example.models.entity;

public class Car {
    private Long id;
    private String brand;

    public Car() {}
    public Car(Long id, String brand) {
        this.id = id;
        this.brand = brand;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
} 