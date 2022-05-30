package cs.hku.hktransportandroid.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

object TimeConverter {
    fun fromStringToTime(string: String): LocalDateTime? {
        return LocalDateTime.parse(string, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}

fun String.toTime(): LocalDateTime? {
    return TimeConverter.fromStringToTime(this)
}
fun LocalDateTime.minutesFromNow(): Long {
    return LocalDateTime.now().until(this,ChronoUnit.MINUTES)
}