package com.example.randomtraveller.core.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun Long.toLocalDate(): LocalDate {
    return Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}

fun LocalDate.toFormattedDateString(): String {
    val dayOfWeek = this.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    val dayOfMonth = this.dayOfMonth
    val year = this.year

    val dayOfMonthSuffix =
        when (dayOfMonth) {
            1, 21, 31 -> "st"
            2, 22 -> "nd"
            3, 23 -> "rd"
            else -> "th"
        }
    return "$dayOfWeek, ${dayOfMonth}$dayOfMonthSuffix of $year"
}

fun LocalDate.toUtcIsoStartOfDayString(): String {
    val localZone = ZoneId.systemDefault()
    val startOfDay = this.atTime(LocalTime.MIDNIGHT)
    val zonedLocal = startOfDay.atZone(localZone)
    val utcZoned = zonedLocal.withZoneSameInstant(ZoneId.of("UTC"))

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return utcZoned.format(formatter)
}

fun LocalDate.toUtcIsoEndOfDayString(): String {
    val localZone = ZoneId.systemDefault()
    val endOfDay = this.atTime(LocalTime.of(23, 59, 59))
    val zonedLocal = endOfDay.atZone(localZone)
    val utcZoned = zonedLocal.withZoneSameInstant(ZoneId.of("UTC"))

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return utcZoned.format(formatter)
}

fun String.toPrettyLocalDate(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    val utcZoned = ZonedDateTime.parse(this, formatter.withZone(ZoneId.of("UTC")))
    val localZoned = utcZoned.withZoneSameInstant(ZoneId.systemDefault())
    val localDate = localZoned.toLocalDate()

    val outputFormatter = DateTimeFormatter.ofPattern("MMM d") // e.g., "May 23"
    return localDate.format(outputFormatter)
}
