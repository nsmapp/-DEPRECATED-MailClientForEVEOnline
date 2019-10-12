package by.nepravskysm.domain.entity

data class Contact(val contactId: Long,
                   val contactType: String,
                   val standing: Double,
                   var contactName: String = "No_name_fix_it")