package com.example.employeedata.helpers;

import com.example.employeedata.exception.CustomValidationException;
import org.apache.logging.log4j.util.Strings;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class DateTimeHelpers {
    public static DateTimeFormatter ZONE_FORMATTER = DateTimeFormatter.ofPattern(Constants.ZONE_DATE_TIME_STRING_FORMAT);
    public static final String RES_NAME = "zoneId/zoneOffset";

    public static LocalDateTime getLocalDateTimeNow() {
        return LocalDateTime.now();
    }

    public static String getLocalDateTimeNow(String dateTimeFormat) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateTimeFormat));
    }

    public static LocalDate getLocalDateNow() {
        return LocalDate.now();
    }

    public static ZonedDateTime getZDTFromLDT(LocalDateTime ldt, String zoneId) {
        return ZonedDateTime.of(ldt, ZoneId.of(zoneId));
    }

    public static ZonedDateTime getZDTFromLDTNow(String zoneId) {
        return LocalDateTime.now().atZone(ZoneId.of(zoneId));
    }

    public static LocalDateTime getLDTFromZDT(ZonedDateTime zdt, String zoneId) {
        return zdt.withZoneSameInstant(ZoneId.of(zoneId)).toLocalDateTime();
    }

    public static ZonedDateTime getFormattedZDTFromString(String dateTime, String zoneId) {
        return ZonedDateTime.parse(dateTime.trim().concat(" " + zoneId), ZONE_FORMATTER);
    }

    public static ZonedDateTime getFormattedZDTFromLDTNow(String zoneId) {
        String zdt = LocalDateTime.now().atZone(ZoneId.of(zoneId)).format(ZONE_FORMATTER);
        return ZonedDateTime.parse(zdt, ZONE_FORMATTER);
    }

    public static void validateZoneId(String zoneId) {
        if (Strings.isNotBlank(zoneId)) {
            Exception exception = null;
            if (Pattern.matches(Constants.REGEX_DATE_OFFSET, zoneId)) {
                try {
                    ZoneOffset.of(zoneId);
                } catch (DateTimeException e) {
                    exception = e;
                }
            } else {
                try {
                    ZoneId.of(zoneId);
                } catch (DateTimeException e) {
                    exception = e;
                }
            }

            if (exception != null) {
                throw new CustomValidationException(RES_NAME, "zoneId/zoneOffset", zoneId);
            }
        } else {
            throw new CustomValidationException(RES_NAME, "cannot be empty");
        }
    }
}
