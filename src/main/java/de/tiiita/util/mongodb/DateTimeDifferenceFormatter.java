package de.tiiita.util.mongodb;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.Period;

public class DateTimeDifferenceFormatter {

    public static String formatDateTimeDifference(OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        Period period = Period.between(startDateTime.toLocalDate(), endDateTime.toLocalDate());
        Duration duration = Duration.between(startDateTime.toLocalTime(), endDateTime.toLocalTime());

        if (duration.isNegative()) {
            duration = duration.plusDays(1);
            period = period.minusDays(1);
        }

        long years = period.getYears();
        long months = period.getMonths();
        long days = period.getDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;

        StringBuilder formattedDuration = new StringBuilder();
        if (years != 0) {
            formattedDuration.append(years).append("y ");
        }
        if (months != 0) {
            formattedDuration.append(months).append("mo ");
        }
        if (days != 0) {
            formattedDuration.append(days).append("d ");
        }
        if (hours != 0) {
            formattedDuration.append(hours).append("h ");
        }
        if (minutes != 0) {
            formattedDuration.append(minutes).append("m");
        }

        return formattedDuration.toString().trim();
    }
}
