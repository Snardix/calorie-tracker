#  Calorie Tracker API

## Описание проекта
**Calorie Tracker API** – это REST API-сервис для отслеживания дневной нормы калорий и учета съеденных блюд.  
Приложение позволяет пользователям регистрироваться, добавлять приемы пищи и контролировать свою норму калорий.  
Алгоритм расчета основан на **формуле Харриса-Бенедикта** с учетом уровня физической активности.

## Технологии
- **Backend:** Java 17, Spring Boot, Spring Data JPA, Hibernate
- **База данных:** PostgreSQL (JSONB для хранения списка блюд)
- **Тестирование:** JUnit 5, Mockito
- **Сборка:** Maven
- **Контейнеризация (опционально):** Docker

## Запуск проекта

### Настройка базы данных
Перед запуском убедитесь, что у вас установлен PostgreSQL.  
Создайте базу данных:
```sql
CREATE DATABASE calorie_tracker;

-- Таблица пользователей
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    age INT CHECK (age > 0),
    weight DECIMAL(5,2) CHECK (weight > 0),
    height DECIMAL(5,2) CHECK (height > 0),
    goal VARCHAR(20) CHECK (goal IN ('Похудение', 'Поддержание', 'Набор массы')),
    activity_level VARCHAR(20) CHECK (activity_level IN ('Низкий', 'Средний', 'Высокий')),
    daily_calories DECIMAL(6,2) NOT NULL
);

-- Таблица блюд
CREATE TABLE dishes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    calories DECIMAL(6,2) NOT NULL CHECK (calories > 0),
    proteins DECIMAL(5,2) CHECK (proteins >= 0),
    fats DECIMAL(5,2) CHECK (fats >= 0),
    carbs DECIMAL(5,2) CHECK (carbs >= 0)
);

-- Таблица приемов пищи (с JSONB для хранения списка блюд)
CREATE TABLE meals (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id) ON DELETE CASCADE,
    list_dishes JSONB NOT NULL,  -- Храним список блюд в JSONB
    meal_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
**```
### Настройка IDE
application.properties: 
spring.datasource.url=jdbc:postgresql://localhost:5432/calorie_tracker
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

### API эндпоинты
Пользователи
Метод	URL	Описание
POST	/users	Создать пользователя
GET	/users/{id}	Получить пользователя по ID
GET	/users/email/{email}	Получить пользователя по Email
PUT	/users/{id}	Обновить пользователя
DELETE	/users/{id}	Удалить пользователя
Приемы пищи
Метод	URL	Описание
POST	/meals	Добавить прием пищи
GET	/meals/today/{userId}Получение приемов пищи за сегодня
GET	/meals/today/{userId}/calories	Получение общей суммы калорий за сегодня
GET	/meals/today/{userId}/within-limit Проверка, уложился ли пользователь в норму калорий
GET	/meals/{userId}/history Получение истории питания по дням
DELETE	/meals/{mealId}	Удаление приема пищи
Блюда
Метод	URL	Описание
POST	/dishes	Добавить блюдо
GET	/dishes	Получить список всех блюд
GET	/dishes/{id}	Получить блюдо по ID
GET	/dishes/name/{name}	Найти блюдо по названию
PUT	/dishes/{id}	Обновить блюдо
DELETE	/dishes/{id}	Удалить блюдо
Отчеты
Метод	URL	Описание
GET	/reports/{userId}/calories-today	Получить потребленные калории за день
GET	/reports/{userId}/within-limit	Проверить, превышена ли норма калорий