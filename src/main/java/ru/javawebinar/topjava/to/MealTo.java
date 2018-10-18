package ru.javawebinar.topjava.to;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MealTo {

    private final LocalDateTime dateTime;
    private final String description;
    private final int calories;
    private final boolean exceed;
    private final Integer id;

    public MealTo(LocalDateTime dateTime, String description, int calories, Integer id, boolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.exceed = exceed;
        this.id = id;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", exceed=" + exceed +
                ", id=" + id +
                '}';
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isExceed() {
        return exceed;
    }

    public Integer getId() {
        return id;
    }

}