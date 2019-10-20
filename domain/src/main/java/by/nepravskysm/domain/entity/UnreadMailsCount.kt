package by.nepravskysm.domain.entity

data class UnreadMailsCount(
    var inbox: Int  = 0,
    var send: Int  = 0,
    var corporation: Int  = 0,
    var alliance: Int  = 0,
    var mailingList: Int = 0
) {
}