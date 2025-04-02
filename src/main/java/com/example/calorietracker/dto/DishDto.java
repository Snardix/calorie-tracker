package com.example.calorietracker.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DishDto {
    @JsonAlias("id")
    private Long dishId;
    private String name;
    private Integer calories;
    private Integer proteins;
    private Integer fats;
    private Integer carbs;
    private Integer quantity;

    public DishDto() {}

    public DishDto(Long dishId, String name, Integer calories, Integer proteins, Integer fats, Integer carbs, Integer quantity) {
        this.dishId = dishId;
        this.name = name;
        this.calories = calories;
        this.proteins = proteins;
        this.fats = fats;
        this.carbs = carbs;
        this.quantity = quantity;
    }

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getProteins() {
        return proteins;
    }

    public void setProteins(Integer proteins) {
        this.proteins = proteins;
    }

    public Integer getFats() {
        return fats;
    }

    public void setFats(Integer fats) {
        this.fats = fats;
    }

    public Integer getCarbs() {
        return carbs;
    }

    public void setCarbs(Integer carbs) {
        this.carbs = carbs;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}