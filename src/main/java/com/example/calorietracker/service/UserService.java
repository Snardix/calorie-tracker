package com.example.calorietracker.service;

import com.example.calorietracker.dto.UserDto;
import com.example.calorietracker.model.User;
import com.example.calorietracker.repository.MealRepository;
import com.example.calorietracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Добавление пользователя
    @Transactional
    public UserDto addUser(UserDto userDto) {
        User user = toEntity(userDto);
        user.calculateDailyCalories();
        user = userRepository.save(user);
        return toDTO(user);
    }

    // Получение пользователя по ID
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    // Получение пользователя по Email
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDTO);
    }

    // Получение всех пользователей
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Обновление данных пользователя
    @Transactional
    public Optional<UserDto> updateUser(Long id, UserDto userDto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(userDto.getName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setAge(userDto.getAge());
            existingUser.setWeight(userDto.getWeight());
            existingUser.setHeight(userDto.getHeight());
            existingUser.setGoal(userDto.getGoal());
            existingUser.setActivityLevel(userDto.getActivityLevel());
            existingUser.calculateDailyCalories();
            return toDTO(userRepository.save(existingUser));
        });
    }

    // Удаление пользователя
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Конвертация DTO -> Entity
    private User toEntity(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAge(userDto.getAge());
        user.setWeight(userDto.getWeight());
        user.setHeight(userDto.getHeight());
        user.setGoal(userDto.getGoal());
        user.setActivityLevel(userDto.getActivityLevel());
        user.setDailyCalories(userDto.getDailyCalories());
        return user;
    }

    // Конвертация Entity -> DTO
    private UserDto toDTO(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getWeight(),
                user.getHeight(),
                user.getGoal(),
                user.getActivityLevel(),
                user.getDailyCalories()
        );
    }
}
