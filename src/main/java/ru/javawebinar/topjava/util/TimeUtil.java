package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_TIME_PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    public static boolean isBetween(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) <= 0;
    }

    public static String toString(LocalDateTime localDateTime) {
        return localDateTime == null ? "" : localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String string) {
        return LocalDateTime.parse(string, DATE_TIME_PARSER);
    }

}
