package by.nepravskysm.domain.utils

import by.nepravskysm.domain.entity.MailHeader

fun listHeadersToUniqueList(list: List<MailHeader>) : MutableList<Long>{

    val idSet = mutableSetOf<Long>()
    for (header in list){
        idSet.add(header.fromId)
    }

    return idSet.toMutableList()
}