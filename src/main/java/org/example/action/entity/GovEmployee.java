package org.example.action.entity;

public class GovEmployee {
    private Long id;
    private String name;

    public GovEmployee() {}
    public GovEmployee(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
} 