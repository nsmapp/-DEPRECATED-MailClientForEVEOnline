package by.nepravskysm.domain.entity

import by.nepravskysm.domain.entity.subentity.Recipient


data class MailHeader (
    val mailId: Long,
    val fromId: Long,
    var fromName:String = "NO NAME, FIX ME",
    val isRead: Boolean,
    val labels: List<Int>,
    val recipients: List<Recipient>,
    val subject: String,
    var timestamp: String = "NO NAME, FIX ME"
)



