package com.example.calorietracker.model;




public enum ActivityLevel {
    Низкий(1.2),
    Средний(1.55),
    Высокий(1.9);

    private final double factor;

    ActivityLevel(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }
}