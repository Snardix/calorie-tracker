package com.example.calorietracker.controller;

import com.example.calorietracker.dto.MealDto;
import com.example.calorietracker.service.DishService;
import com.example.calorietracker.service.MealService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.calorietracker.repository.UserRepository;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/meals")
public class MealController {

    private final MealService mealService;
    private final UserRepository userRepository;

    public MealController(MealService mealService, UserRepository userRepository) {
        this.mealService = mealService;
        this.userRepository = userRepository;
    }

    // Добавление приема пищи
    @PostMapping
    public ResponseEntity<MealDto> addMeal(@RequestBody MealDto mealDto) {
        return mealService.addMeal(mealDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получение приемов пищи за сегодня
    @GetMapping("/today/{userId}")
    public ResponseEntity<?> getMealsForToday(@PathVariable Long userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Пользователь с id " + userId + " не найден");
        }

        List<MealDto> meals = mealService.getMealsForToday(userId);
        return ResponseEntity.ok(meals);
    }

    // Получение общей суммы калорий за сегодня
    @GetMapping("/today/{userId}/calories")
    public ResponseEntity<Integer> getTotalCaloriesForToday(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.getTotalCaloriesForToday(userId));
    }

    // Проверка, уложился ли пользователь в норму калорий
    @GetMapping("/today/{userId}/within-limit")
    public ResponseEntity<Boolean> isWithinCalorieLimit(@PathVariable Long userId) {
        return ResponseEntity.ok(mealService.isWithinCalorieLimit(userId));
    }

    // Получение истории питания по дням
    @GetMapping("/{userId}/history")
    public ResponseEntity<List<MealDto>> getMealsByDate(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now(); // Если дата не передана, берем текущий день
        }
        return ResponseEntity.ok(mealService.getMealsByDate(userId, date));
    }

    // Удаление приема пищи
    @DeleteMapping("/{mealId}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long mealId) {
        mealService.deleteMeal(mealId);
        return ResponseEntity.noContent().build();
    }
}
