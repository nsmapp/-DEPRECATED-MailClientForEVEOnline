package by.nepravskysm.domain.utils

import by.nepravskysm.domain.entity.MailHeader
import java.text.SimpleDateFormat
import java.util.*

fun convertDataFormatInMailHeader(header: MailHeader): MailHeader{

    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
    inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dayDateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy")
    dayDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    header.timestamp = dayDateFormat.format(inputDateFormat.parse(header.timestamp))

    return header
}

