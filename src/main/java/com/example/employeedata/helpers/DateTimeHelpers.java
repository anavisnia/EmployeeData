package com.example.employeedata.helpers;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeHelpers {
    public static DateTimeFormatter ZONE_FORMATTER = DateTimeFormatter.ofPattern(Constants.ZONE_DATE_TIME_STRING_FORMAT);
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

    public static ZonedDateTime GetFormattedZDTFromString(String dateTime, String zoneId) {
        return ZonedDateTime.parse(dateTime.trim().concat(" " + zoneId), ZONE_FORMATTER);
    }

    public static ZonedDateTime GetFormattedZDTFromLDTNow(String zoneId) {
        String zdt = LocalDateTime.now().atZone(ZoneId.of(zoneId)).format(ZONE_FORMATTER);
        return ZonedDateTime.parse(zdt, ZONE_FORMATTER);
    }
}
