package com.example.foodplaner.model;

public class IngredientItem {
    private String name;
    private String measurement;
    private String imageUrl;

    public IngredientItem(String name, String measurement, String imageUrl) {
        this.name = name;
        this.measurement = measurement;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getName() { return name; }
    public String getMeasurement() { return measurement; }
    public String getImageUrl() { return imageUrl; }
}
