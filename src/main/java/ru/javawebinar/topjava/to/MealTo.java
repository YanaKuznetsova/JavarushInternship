package ru.javawebinar.topjava.to;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class MealTo {

    private LocalDateTime dateTime;
    private String description;
    private int calories;
    private boolean excess;
    private Integer id;

    public MealTo(LocalDateTime dateTime, String description, int calories, Integer id, boolean exceed) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = exceed;
        this.id = id;
    }

    public MealTo() {
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
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

    public boolean isExcess() {
        return excess;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MealTo that = (MealTo) o;
        return calories == that.calories &&
                excess == that.excess &&
                Objects.equals(id, that.id) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dateTime, description, calories, excess);
    }

}