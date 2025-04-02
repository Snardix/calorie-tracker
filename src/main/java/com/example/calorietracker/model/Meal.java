package com.example.calorietracker.model;

import com.example.calorietracker.dto.DishDto;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JdbcTypeCode(org.hibernate.type.SqlTypes.JSON) // Указываем, что это JSONB
    @Column(name = "list_dishes", columnDefinition = "jsonb", nullable = false)
    private List<DishDto> listDishes;

    @Column(nullable = false)
    private LocalDateTime mealTime;

    public Meal() {}

    public Meal(Long id, User user, List<DishDto> listDishes, LocalDateTime mealTime) {
        this.id = id;
        this.user = user;
        this.listDishes = listDishes;
        this.mealTime = mealTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
