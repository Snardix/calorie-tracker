package com.example.calorietracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private double height;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Goal goal;  // Цель (Похудение, Поддержание, Набор массы)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;  // Уровень активности

    @Column(nullable = false)
    private double dailyCalories; // Рассчитывается по формуле

    public User() {}

    public User(Long id, String name, String email, int age, double weight, double height, Goal goal, ActivityLevel activityLevel, double dailyCalories) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.goal = goal;
        this.activityLevel = activityLevel;
        this.dailyCalories = dailyCalories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }

    public double getDailyCalories() {
        return dailyCalories;
    }

    public void setDailyCalories(double dailyCalories) {
        this.dailyCalories = dailyCalories;
    }

    // Автоматический расчет нормы калорий
    public void calculateDailyCalories() {
        double bmr;
        if (goal == Goal.Похудение) {
            bmr = 10 * weight + 6.25 * height - 5 * age - 500;
        } else if (goal == Goal.Набор_массы) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 500;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age;
        }

        // Умножаем на коэффициент активности
        dailyCalories = bmr * activityLevel.getFactor();
    }
}
