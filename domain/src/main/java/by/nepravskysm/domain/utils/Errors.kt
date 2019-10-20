package by.nepravskysm.domain.utils

//AUTH
const val AUTH_ERROR: Long = 47000100


//ESI
const val SYNCHRONIZE_CONTACT_ERROR: Long = 46000100

const val SYNCHRONIZE_MAIL_HEADER_ERROR: Long = 46000301
const val LOAD_NEW_MAIL_HEADER_ERROR: Long = 46000302

const val SEND_MAIL_ERROR: Long = 46000400
const val GET_MAIL_ERROR: Long = 46000501
const val UPDATE_MAIL_METADATA_ERROR: Long  = 46000502
const val DELETE_MAIL_FROM_SERVER_ERROR: Long = 46000503

//Database
const val DB_ACTIVE_CHARACTER_INFO_ERROR: Long = 45000000
const val DB_LOAD_MAIL_HEADER_FROM_DATABASE: Long = 45000301
const val DB_UPDATE_MAIL_METADATA_ERROR: Long = 45000302
const val DB_DELETE_MAIL_ERROR: Long = 45000303
