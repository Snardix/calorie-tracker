package com.example.calorietracker;

import com.example.calorietracker.dto.UserDto;
import com.example.calorietracker.model.ActivityLevel;
import com.example.calorietracker.model.Goal;
import com.example.calorietracker.model.User;
import com.example.calorietracker.repository.UserRepository;
import com.example.calorietracker.service.UserService;
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

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser_Success() {
        UserDto userDto = new UserDto(null, "John Doe", "john@example.com", 25, 75.0, 180.0, Goal.Поддержание, ActivityLevel.Средний, 2500);
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setAge(25);
        user.setWeight(75.0);
        user.setHeight(180.0);
        user.setGoal(Goal.Поддержание);
        user.setActivityLevel(ActivityLevel.Средний);
        user.setDailyCalories(2500);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto savedUser = userService.addUser(userDto);

        assertNotNull(savedUser);
        assertEquals(1L, savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals(Goal.Поддержание, savedUser.getGoal());
        assertEquals(ActivityLevel.Средний, savedUser.getActivityLevel());
    }

    @Test
    void testGetUserById_Found() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setGoal(Goal.Похудение);
        user.setActivityLevel(ActivityLevel.Высокий);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        assertEquals(Goal.Похудение, result.get().getGoal());
        assertEquals(ActivityLevel.Высокий, result.get().getActivityLevel());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.getUserById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserByEmail_Found() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        user.setGoal(Goal.Набор_массы);
        user.setActivityLevel(ActivityLevel.Низкий);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        Optional<UserDto> result = userService.getUserByEmail("john@example.com");

        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
        assertEquals(Goal.Набор_массы, result.get().getGoal());
        assertEquals(ActivityLevel.Низкий, result.get().getActivityLevel());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.getUserByEmail("john@example.com");

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllUsers_ReturnsList() {
        List<User> users = Arrays.asList(
                new User(1L, "John Doe", "john@example.com", 25, 75.0, 180.0, Goal.Поддержание, ActivityLevel.Средний, 2500),
                new User(2L, "Jane Doe", "jane@example.com", 30, 65.0, 170.0, Goal.Похудение, ActivityLevel.Низкий, 2000)
        );

        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals(Goal.Поддержание, result.get(0).getGoal());
        assertEquals(ActivityLevel.Средний, result.get(0).getActivityLevel());

        assertEquals("Jane Doe", result.get(1).getName());
        assertEquals(Goal.Похудение, result.get(1).getGoal());
        assertEquals(ActivityLevel.Низкий, result.get(1).getActivityLevel());
    }

    @Test
    void testUpdateUser_Found() {
        // Исходные данные пользователя
        User existingUser = new User(1L, "John Doe", "john@example.com", 25, 75.0, 180.0, Goal.Поддержание, ActivityLevel.Средний, 2500);

        // Обновленные данные
        UserDto updatedDto = new UserDto(1L, "John Updated", "john@example.com", 26, 80.0, 181.0, Goal.Набор_массы, ActivityLevel.Высокий, 4372.375);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.calculateDailyCalories(); // Применяем расчет
            return savedUser;
        });

        Optional<UserDto> result = userService.updateUser(1L, updatedDto);

        assertTrue(result.isPresent());
        assertEquals("John Updated", result.get().getName());
        assertEquals(26, result.get().getAge());
        assertEquals(Goal.Набор_массы, result.get().getGoal());
        assertEquals(ActivityLevel.Высокий, result.get().getActivityLevel());
        assertEquals(4372.375, result.get().getDailyCalories()); // Ожидаем правильное значение
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDto> result = userService.updateUser(1L, new UserDto());

        assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
