package com.example.employeedata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;

import java.time.*;

class ProjectServiceTests {

    @Test
    public void givenTimeZone_whenFetchingData_thenReturnsAllProjectWithSetTimeZone() {
        ZoneOffset zoneOffset = ZoneOffset.of("+02:00");
        String initialDate = "2029-01-01T18:00:00+01:00";
        String expectedDate = "2029-01-01T19:00:00+02:00";

        ZonedDateTime initialParsedAndZoneAdded = ZonedDateTime.parse(initialDate).withZoneSameInstant(zoneOffset);
        ZonedDateTime expectedOutcome = ZonedDateTime.parse(expectedDate);

        assertEquals(expectedOutcome, initialParsedAndZoneAdded);
    }

}
