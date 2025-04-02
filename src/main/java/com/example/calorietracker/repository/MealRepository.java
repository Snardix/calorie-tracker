package com.example.calorietracker.repository;

import com.example.calorietracker.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByUserId(Long userId); // Вся история питания

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND CAST(m.mealTime AS date) = CURRENT_DATE")
    List<Meal> findMealsForToday(@Param("userId") Long userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id = :userId AND m.mealTime BETWEEN :start AND :end")
    List<Meal> findMealsByDateRange(@Param("userId") Long userId,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end); // История по дням
}