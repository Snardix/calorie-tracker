package com.example.calorietracker;

import com.example.calorietracker.dto.DishDto;
import com.example.calorietracker.model.Dish;
import com.example.calorietracker.repository.DishRepository;
import com.example.calorietracker.service.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private DishService dishService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddDish_Success() {
        DishDto dishDto = new DishDto(null, "Apple", 52, 2, 3, 14, null);
        Dish dish = new Dish(1L, "Apple", 52, 2, 3, 14);

        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        DishDto savedDish = dishService.addDish(dishDto);

        assertNotNull(savedDish);
        assertEquals(1L, savedDish.getDishId());
        assertEquals("Apple", savedDish.getName());
    }

    @Test
    void testGetAllDishes_ReturnsList() {
        List<Dish> dishes = Arrays.asList(
                new Dish(1L, "Apple", 52, 2, 3, 14),
                new Dish(2L, "Banana", 96, 2, 3, 27)
        );

        when(dishRepository.findAll()).thenReturn(dishes);

        List<DishDto> result = dishService.getAllDishes();

        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).getName());
        assertEquals("Banana", result.get(1).getName());
    }

    @Test
    void testGetDishById_Found() {
        Dish dish = new Dish(1L, "Apple", 52, 2, 3, 14);
        when(dishRepository.findById(1L)).thenReturn(Optional.of(dish));

        Optional<DishDto> result = dishService.getDishById(1L);

        assertTrue(result.isPresent());
        assertEquals("Apple", result.get().getName());
    }

    @Test
    void testGetDishById_NotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<DishDto> result = dishService.getDishById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetDishByName_Found() {
        Dish dish = new Dish(1L, "Apple", 52, 2, 3, 14);
        when(dishRepository.findByName("Apple")).thenReturn(Optional.of(dish));

        Optional<DishDto> result = dishService.getDishByName("Apple");

        assertTrue(result.isPresent());
        assertEquals(52, result.get().getCalories());
    }

    @Test
    void testGetDishByName_NotFound() {
        when(dishRepository.findByName("Apple")).thenReturn(Optional.empty());

        Optional<DishDto> result = dishService.getDishByName("Apple");

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateDish_Found() {
        Dish existingDish = new Dish(1L, "Apple", 52, 2, 3, 14);
        DishDto updatedDto = new DishDto(1L, "Green Apple", 48, 2, 3, 12, null);

        when(dishRepository.findById(1L)).thenReturn(Optional.of(existingDish));
        when(dishRepository.save(any(Dish.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<DishDto> result = dishService.updateDish(1L, updatedDto);

        assertTrue(result.isPresent());
        assertEquals("Green Apple", result.get().getName());
        assertEquals(48, result.get().getCalories());
    }

    @Test
    void testUpdateDish_NotFound() {
        when(dishRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<DishDto> result = dishService.updateDish(1L, new DishDto());

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteDish() {
        doNothing().when(dishRepository).deleteById(1L);

        dishService.deleteDish(1L);

        verify(dishRepository, times(1)).deleteById(1L);
    }
}
