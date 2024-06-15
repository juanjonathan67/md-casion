package com.example.casion.util

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val DATE_TIME_PATTERN = "EE, d MMMM yyyy HH:mm"
private const val DATE_PATTERN = "d/M/yy"
private const val TIME_PATTERN = "HH:mm"

object Time {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)
    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN)
    private val timeFormatter = DateTimeFormatter.ofPattern(TIME_PATTERN)

    fun localDateTimeParser(dateTime: String) : LocalDateTime = LocalDateTime.parse(dateTime, dateTimeFormatter)

    fun getCurrentTime(): String = LocalTime.now().format(timeFormatter)

    fun getCurrentDateTime(): String = LocalDateTime.now().format(dateTimeFormatter)

    fun getDateFromDateTime(dateTime: String) : String = localDateTimeParser(dateTime).toLocalDate().format(dateFormatter)

    fun getTimeFromDateTime(dateTime: String) : String = localDateTimeParser(dateTime).format(timeFormatter)

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
}