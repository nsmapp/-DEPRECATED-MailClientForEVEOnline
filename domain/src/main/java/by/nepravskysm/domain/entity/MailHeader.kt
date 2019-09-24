package by.nepravskysm.domain.entity

import by.nepravskysm.domain.entity.subentity.Recipient


data class MailHeader (
    val from: Long,
    val isRead: Boolean,
    val labels: List<Int>,
    val mailId: Long,
    val recipients: List<Recipient>,
    val subject: String,
    var timestamp: String
){


    var fromName = ""
}