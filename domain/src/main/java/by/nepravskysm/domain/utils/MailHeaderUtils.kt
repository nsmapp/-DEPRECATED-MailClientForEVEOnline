package by.nepravskysm.domain.utils

import by.nepravskysm.domain.entity.MailHeader

fun setMailTypeSenderNameAndDateFormat(
    headerList: MutableList<MailHeader>,
    senderNameMap: Map<Long, String>
)
        : MutableList<MailHeader> {

    headerList.forEach { header ->

        header.fromName = senderNameMap[header.fromId] ?: "Name not found"

        if (header.labels.contains(1)) header.mailType = "Inbox"
        if (header.labels.contains(4)) header.mailType = "Corporation"
        if (header.labels.contains(8)) header.mailType = "Alliance"
        if (header.labels.contains(2)) {
            if (header.labels.size == 1) header.mailType = "Sent" else header.mailType += ", Sent"
        }
        if (header.labels.isEmpty()) {
            header.recipients.forEach {
                header.mailType += " ${senderNameMap[it.id] ?: "Name not found"}"
            }
        }
        convertDataFormatInMailHeader(header)
    }

    return headerList
}





