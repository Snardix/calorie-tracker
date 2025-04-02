package com.example.calorietracker;

import com.example.calorietracker.dto.DishDto;
import com.example.calorietracker.dto.MealDto;
import com.example.calorietracker.model.Meal;
import com.example.calorietracker.model.User;
import com.example.calorietracker.repository.MealRepository;
import com.example.calorietracker.repository.UserRepository;
import com.example.calorietracker.service.MealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MealService mealService;

    private User user;
    private Meal meal;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(2L);
        user.setDailyCalories(2000);

        meal = new Meal();
        meal.setId(1L);
        meal.setUser(user);
    }

    @Test
    void testAddMeal_UserExists_Success() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(mealRepository.save(any(Meal.class))).thenReturn(meal);

        MealDto mealDto = new MealDto();
        mealDto.setUserId(2L);
        mealDto.setListDishes(List.of()); // Должен быть непустой список, пусть даже пустой

        Optional<MealDto> savedMeal = mealService.addMeal(mealDto);

        assertTrue(savedMeal.isPresent());
        verify(mealRepository, times(1)).save(any(Meal.class));
    }

    @Test
    void testAddMeal_UserNotFound_ReturnsEmptyOptional() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        MealDto mealDto = new MealDto();
        mealDto.setUserId(2L);

        Optional<MealDto> result = mealService.addMeal(mealDto);

        assertTrue(result.isEmpty());
        verify(mealRepository, never()).save(any(Meal.class));
    }

    @Test
    void testDeleteMeal_Success() {
        doNothing().when(mealRepository).deleteById(1L);

        assertDoesNotThrow(() -> mealService.deleteMeal(1L));
        verify(mealRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetMealsForToday_UserExists_ReturnsMeals() {
        when(userRepository.existsById(2L)).thenReturn(true);
        when(mealRepository.findMealsForToday(2L)).thenReturn(List.of(meal));

        List<MealDto> meals = mealService.getMealsForToday(2L);

        assertFalse(meals.isEmpty());
        verify(mealRepository, times(1)).findMealsForToday(2L);
    }

    @Test
    void testGetMealsForToday_UserNotFound_ThrowsException() {
        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> mealService.getMealsForToday(2L));
        verify(mealRepository, never()).findMealsForToday(2L);
    }

    @Test
    void testGetTotalCaloriesForToday_ReturnsCorrectSum() {
        meal.setListDishes(List.of(new DishDto(1L, "Apple", 100, 0, 0, 25, 1))); // Добавляем блюда

        when(mealRepository.findMealsForToday(2L)).thenReturn(List.of(meal));

        int totalCalories = mealService.getTotalCaloriesForToday(2L);

        assertEquals(100, totalCalories);
        verify(mealRepository, times(1)).findMealsForToday(2L);
    }

    @Test
    void testIsWithinCalorieLimit_UserExists_ReturnsTrueOrFalse() {
        // Устанавливаем дневную норму пользователя
        user.setDailyCalories(2000);

        // Добавляем прием пищи с блюдами
        meal.setListDishes(List.of(
                new DishDto(1L, "Apple", 500, 0, 0, 100, 1),
                new DishDto(2L, "Chicken", 600, 50, 10, 5, 1)
        ));

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));
        when(mealRepository.findMealsForToday(2L)).thenReturn(List.of(meal));

        boolean withinLimit = mealService.isWithinCalorieLimit(2L);

        assertTrue(withinLimit); // Общая сумма калорий 1100 <= 2000
        verify(mealRepository, times(1)).findMealsForToday(2L);
    }
}