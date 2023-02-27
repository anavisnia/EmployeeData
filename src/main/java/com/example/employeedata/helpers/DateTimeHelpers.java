package com.example.employeedata.helpers;

import java.text.Format;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

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

    public static ZonedDateTime GetZDTFromLDT(LocalDateTime ldt, String zoneId) {
        return ZonedDateTime.of(ldt, ZoneId.of(zoneId));
    }

    public static ZonedDateTime GetZDTFromLDTNow(String zoneId) {
        return LocalDateTime.now().atZone(ZoneId.of(zoneId));
    }

    public static LocalDateTime GetLDTFromZDT(ZonedDateTime zdt, String zoneId) {
        return zdt.withZoneSameInstant(ZoneId.of(zoneId)).toLocalDateTime();
    }
}
