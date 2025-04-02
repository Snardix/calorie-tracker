package com.example.calorietracker.service;

import com.example.calorietracker.dto.DishDto;
import com.example.calorietracker.model.Dish;
import com.example.calorietracker.repository.DishRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    // Создание блюда
    public DishDto addDish(DishDto dishDto) {
        Dish dish = toEntity(dishDto);
        Dish savedDish = dishRepository.save(dish);
        return toDto(savedDish);
    }

    // Получение всех блюд
    public List<DishDto> getAllDishes() {
        return dishRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Получение блюда по ID
    public Optional<DishDto> getDishById(Long id) {
        return dishRepository.findById(id).map(this::toDto);
    }

    // Получение блюда по названию
    public Optional<DishDto> getDishByName(String name) {
        return dishRepository.findByName(name).map(this::toDto);
    }

    // Обновление блюда
    public Optional<DishDto> updateDish(Long id, DishDto dishDto) {
        return dishRepository.findById(id).map(existingDish -> {
            existingDish.setName(dishDto.getName());
            existingDish.setCalories(dishDto.getCalories());
            existingDish.setProteins(dishDto.getProteins());
            existingDish.setFats(dishDto.getFats());
            existingDish.setCarbs(dishDto.getCarbs());
            return toDto(dishRepository.save(existingDish));
        });
    }

    // Удаление блюда
    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    // Конвертация DTO -> Entity
    private Dish toEntity(DishDto dto) {
        return new Dish(
                dto.getDishId(),
                dto.getName(),
                dto.getCalories(),
                dto.getProteins(),
                dto.getFats(),
                dto.getCarbs()
        );
    }

    private DishDto toDto(Dish dish) {
        return new DishDto(
                dish.getId(),
                dish.getName(),
                dish.getCalories(),
                dish.getProteins(),
                dish.getFats(),
                dish.getCarbs(),
                null // quantity не хранится в Dish
        );
    }
}
