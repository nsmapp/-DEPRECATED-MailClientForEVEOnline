package by.nepravskysm.domain.utils

import by.nepravskysm.domain.entity.MailHeader

fun listHeadersToUniqueAttay(list: List<MailHeader>) : Array<Long>{

    val idSet = mutableSetOf<Long>()
    for (header in list){
        idSet.add(header.fromId)
    }

    return idSet.toTypedArray()
}