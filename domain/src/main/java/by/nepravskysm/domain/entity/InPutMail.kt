package by.nepravskysm.domain.entity

data class InPutMail(
    val body: String ,
    val from: Long ,
    val labels: List<Int>,
    val isRead: Boolean,
    val subject: String ,
    val timestamp: String){

//    var fromName = ""
}