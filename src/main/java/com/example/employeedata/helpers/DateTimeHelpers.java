package com.example.employeedata.helpers;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeHelpers {
    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }

    public static String getLocalDateTimeNow(String dateTimeFormat) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalDate getLocalDateNow() {
        return LocalDate.now();
    }

    public static String getLocalDateNow(String dateFormat) {
        return LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat));
    }
}
