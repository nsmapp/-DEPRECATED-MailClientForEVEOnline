package by.nepravskysm.domain.utils

import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.entity.MailingList
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

fun formaMailHeaderList(nameMap: HashMap<Long, String>, headerList: List<MailHeader>): List<MailHeader>{

    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
    inputDateFormat.timeZone = TimeZone.getTimeZone("UTC")
    val dayDateFormat = SimpleDateFormat("HH:mm dd.MM.yyyy")
    dayDateFormat.timeZone = TimeZone.getTimeZone("UTC")

    for (header in headerList){

        header.fromName = nameMap[header.fromId].toString()
        header.timestamp = dayDateFormat.format(inputDateFormat.parse(header.timestamp))
    }

    return headerList
}


fun removeUnsubscrubeMailingList(headerList: MutableList<MailHeader>,
                              mailingList: List<MailingList>):MutableList<MailHeader>{

    val mailHeadersList = mutableListOf<MailHeader>()
    val mailingListHeaders = mutableListOf<MailHeader>()
    for(header in headerList){
        if(header.labels.isNotEmpty()){
            mailHeadersList.add(header)
        }else{
            mailingListHeaders.add(header)
        }
    }

    for(header in mailingListHeaders){
        for(recipients in header.recipients){

        }
    }
    return  mailHeadersList
}