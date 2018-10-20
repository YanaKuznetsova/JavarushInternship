package ru.javawebinar.topjava.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_TIME_PARSER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    public static final LocalDate MIN_DATE = LocalDate.of(1, 1, 1);
    public static final LocalDate MAX_DATE = LocalDate.of(3000, 1, 1);


    public static String toString(LocalDateTime localDateTime) {
        return localDateTime == null ? "" : localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String string) {
        return LocalDateTime.parse(string, DATE_TIME_PARSER);
    }

}
