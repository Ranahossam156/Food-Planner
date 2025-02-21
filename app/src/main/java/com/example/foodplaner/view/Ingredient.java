package com.example.foodplaner.view;

public class Ingredient {
    private int imageRes;
    private String quantity;
    private String name;

    public Ingredient(int imageRes, String quantity, String name) {
        this.imageRes = imageRes;
        this.quantity = quantity;
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}

