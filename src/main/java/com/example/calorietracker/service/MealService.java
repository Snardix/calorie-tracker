package com.example.calorietracker.service;

import com.example.calorietracker.dto.MealDto;
import com.example.calorietracker.dto.DishDto;
import com.example.calorietracker.model.Meal;
import com.example.calorietracker.model.User;
import com.example.calorietracker.repository.DishRepository;
import com.example.calorietracker.repository.MealRepository;
import com.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class MealService {

    private final MealRepository mealRepository;
    private final UserRepository userRepository;
    private final DishRepository dishRepository; // Добавь в сервис

    public MealService(MealRepository mealRepository, UserRepository userRepository, DishRepository dishRepository) {
        this.mealRepository = mealRepository;
        this.userRepository = userRepository;
        this.dishRepository = dishRepository;
    }

    @Transactional
    public Optional<MealDto> addMeal(MealDto mealDto) {
        return userRepository.findById(mealDto.getUserId()).map(user -> {
            Meal meal = toEntity(mealDto, user);
            meal = mealRepository.save(meal);
            return toDTO(meal);
        });
    }

    // Получение приемов пищи за сегодня
    public List<MealDto> getMealsForToday(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }

        return mealRepository.findMealsForToday(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    // Подсчет суммы калорий за сегодня
    public int getTotalCaloriesForToday(Long userId) {
        return mealRepository.findMealsForToday(userId).stream()
                .flatMap(meal -> meal.getListDishes().stream().map(DishDto::getCalories)) // Теперь сразу работаем с List<DishDto>
                .mapToInt(Integer::intValue)
                .sum();
    }

    // Проверка, уложился ли пользователь в норму калорий
    public boolean isWithinCalorieLimit(Long userId) {
        return userRepository.findById(userId).map(user ->
                getTotalCaloriesForToday(userId) <= user.getDailyCalories()
        ).orElse(false);
    }

    // Получение истории питания по дням
    public List<MealDto> getMealsByDate(Long userId, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return mealRepository.findMealsByDateRange(userId, startOfDay, endOfDay)
                .stream().map(this::toDTO).toList();
    }

    // Удаление приема пищи
    @Transactional
    public void deleteMeal(Long mealId) {
        mealRepository.deleteById(mealId);
    }

    private List<DishDto> fetchDishesFromDB(List<DishDto> dishDtos) {
        return dishDtos.stream().map(dishDto -> {
            if (dishDto.getDishId() == null) {
                throw new IllegalArgumentException("Ошибка: dishId не может быть null");
            }

            return dishRepository.findById(dishDto.getDishId())
                    .map(dish -> new DishDto(
                            dish.getId(),  // Используем dishId
                            dish.getName(),
                            dish.getCalories(),
                            dish.getProteins(),
                            dish.getFats(),
                            dish.getCarbs(),
                            dishDto.getQuantity() // Количество из запроса
                    ))
                    .orElseThrow(() -> new RuntimeException("Блюдо с id " + dishDto.getDishId() + " не найдено"));
        }).toList();
    }

    // Конвертация DTO -> Entity
    private Meal toEntity(MealDto mealDto, User user) {
        Meal meal = new Meal();
        meal.setUser(user);
        meal.setListDishes(fetchDishesFromDB(mealDto.getListDishes())); // Загружаем актуальные данные
        meal.setMealTime(mealDto.getMealTime());
        return meal;
    }

    // Конвертация Entity -> DTO
    private MealDto toDTO(Meal meal) {
        return new MealDto(
                meal.getId(),
                meal.getUser().getId(),
                meal.getListDishes(), // Теперь сразу используем List<DishDto>
                meal.getMealTime()
        );
    }
}
