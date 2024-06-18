package com.example.casion.util

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.Period
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val DATE_TIME_PATTERN = "EE, d MMMM yyyy HH:mm"
private const val DATE_PATTERN = "yyyy-MM-dd"
private const val DATE_READABLE_PATTERN = "EE, d MMMM yyyy"
private const val TIME_PATTERN = "HH:mm"

object Time {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN, Locale.US)
    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    private val dateReadableFormatter = DateTimeFormatter.ofPattern(DATE_READABLE_PATTERN, Locale.US)
    private val timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)

    fun localDateTimeParser(dateTime: String) : LocalDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter)

    fun localDateParser(date: String): LocalDate = LocalDate.parse(date)

    fun zonedDateTimeParser(zonedDateTime: String): ZonedDateTime = ZonedDateTime.ofInstant(Instant.parse(zonedDateTime), ZoneId.of("UTC"))

    fun getCurrentTime(): String = LocalTime.now().format(timeFormatter)

    fun getCurrentDateTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    fun getDateFromDateTime(dateTime: String) : String = localDateTimeParser(dateTime).toLocalDate().format(dateFormatter)

    fun getDateFromZonedDateTime(zonedDateTime: String) : String = zonedDateTimeParser(zonedDateTime).format(dateFormatter)

    fun getReadableDateFromDate(date: String) : String = localDateParser(date).format(dateReadableFormatter)

    fun getTimeFromDateTime(dateTime: String) : String = localDateTimeParser(dateTime).format(timeFormatter)

    fun getTimeFromZonedDateTime(zonedDateTime: String) : String = zonedDateTimeParser(zonedDateTime).format(timeFormatter)

    fun getDateTimeFromZonedDateTime(zonedDateTime: String) : String = zonedDateTimeParser(zonedDateTime).format(dateTimeFormatter)

    fun isTomorrowOrAfter(localDateTime: LocalDateTime) : Boolean = localDateTime.dayOfMonth < LocalDateTime.now().dayOfMonth

    fun getTimeOrDateRelative(dateTime: String) : String {
        return if (isTomorrowOrAfter(localDateTimeParser(dateTime))) {
            getDateFromDateTime(dateTime)
        } else {
            getTimeFromDateTime(dateTime)
        }
    }

    fun localDateFromTimestamp(timestamp: Long): LocalDate = Instant.ofEpochMilli(timestamp).atZone(
        ZoneId.systemDefault()).toLocalDate()

    fun getAgeFromLocalDate(localDate: String): String = Period.between(LocalDate.parse(localDate, dateFormatter), LocalDate.now()).years.toString()


}