package com.example.calorietracker.dto;

import java.time.LocalDateTime;
import java.util.List;

public class MealDto {
    private Long id;
    private Long userId;
    private List<DishDto> listDishes; // Список блюд в нормальном формате
    private LocalDateTime mealTime;

    public MealDto() {}

    public MealDto(Long id, Long userId, List<DishDto> listDishes, LocalDateTime mealTime) {
        this.id = id;
        this.userId = userId;
        this.listDishes = listDishes;
        this.mealTime = mealTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<DishDto> getListDishes() {
        return listDishes;
    }

    public void setListDishes(List<DishDto> listDishes) {
        this.listDishes = listDishes;
    }

    public LocalDateTime getMealTime() {
        return mealTime;
    }

    public void setMealTime(LocalDateTime mealTime) {
        this.mealTime = mealTime;
    }
}
