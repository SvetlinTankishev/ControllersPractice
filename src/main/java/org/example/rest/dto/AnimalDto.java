package org.example.rest.dto;

public class AnimalDto {
    private String type;
    public AnimalDto() {}
    public AnimalDto(String type) { this.type = type; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
} 