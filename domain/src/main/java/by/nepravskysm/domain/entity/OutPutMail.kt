package by.nepravskysm.domain.entity

import by.nepravskysm.domain.entity.subentity.Recipient

class OutPutMail(
    val approvedCost: Int = 0,
    var body: String,
    val recipients: MutableList<Recipient>,
    val subject: String
)