package by.nepravskysm.domain.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun getReinforceTime(day :Long, hour :Long, min :Long):Long {

    return Date().time +
            TimeUnit.DAYS.toMillis(day) +
            TimeUnit.HOURS.toMillis(hour) +
            TimeUnit.MINUTES.toMillis(min)
}

fun getEVETime(days: Long, hours: Long, minuts: Long) :String{
    val unixTime = getReinforceTime(days, hours, minuts)
    val timeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    timeFormat.timeZone = TimeZone.getTimeZone("UTC")

    return timeFormat.format(unixTime)

}

fun getLocalTime(days: Long, hours: Long, minuts: Long) :String{
    val unixTime = getReinforceTime(days, hours, minuts)
    val timeFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    return timeFormat.format(unixTime)
}

