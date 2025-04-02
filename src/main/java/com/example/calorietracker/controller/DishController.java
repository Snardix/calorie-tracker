package com.example.calorietracker.controller;

import com.example.calorietracker.dto.DishDto;
import com.example.calorietracker.service.DishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dishes")
public class DishController {

    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    // Создание блюда
    @PostMapping
    public ResponseEntity<DishDto> createDish(@RequestBody DishDto dishDto) {
        return ResponseEntity.ok(dishService.addDish(dishDto));
    }

    // Получение всех блюд
    @GetMapping
    public ResponseEntity<List<DishDto>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    // Получение блюда по ID
    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getDishById(@PathVariable Long id) {
        return dishService.getDishById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получение блюда по названию
    @GetMapping("/name/{name}")
    public ResponseEntity<DishDto> getDishByName(@PathVariable String name) {
        return dishService.getDishByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Обновление данных блюда
    @PutMapping("/{id}")
    public ResponseEntity<DishDto> updateDish(@PathVariable Long id, @RequestBody DishDto dishDto) {
        return dishService.updateDish(id, dishDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Удаление блюда
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        dishService.deleteDish(id);
        return ResponseEntity.noContent().build();
    }
}
