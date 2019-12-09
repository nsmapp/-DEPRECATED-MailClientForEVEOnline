package by.nepravskysm.mailclientforeveonline.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import by.nepravskysm.domain.utils.*
import by.nepravskysm.mailclientforeveonline.R

fun makeToastMessage(context: Context, messageId: Long){

    val pref = context.getSharedPreferences(SETTINGS, MODE_PRIVATE)
    val isAuth = pref.getBoolean(CHARACTER_IS_AUTH, false)

    if (isAuth) {
        when (messageId) {
            AUTH_ERROR ->
                showToast(context, context.getString(R.string.autg_error))
            SYNCHRONIZE_CONTACT_ERROR ->
                showToast(context, context.getString(R.string.sync_contact_error))
            SYNCHRONIZE_MAIL_HEADER_ERROR ->
                showToast(context, context.getString(R.string.mail_cync_error))
            LOAD_NEW_MAIL_HEADER_ERROR ->
                showToast(context, context.getString(R.string.mail_cync_error))
            SEND_MAIL_ERROR ->
                showToast(context, context.getString(R.string.send_mail_error))
            GET_MAIL_ERROR ->
                showToast(context, context.getString(R.string.recive_mail_error))
            UPDATE_MAIL_METADATA_ERROR ->
                showToast(context, context.getString(R.string.update_mail_metadata_error))
            DB_DELETE_MAIL_ERROR ->
                showToast(context, context.getString(R.string.delete_mail_error))
            MAIL_IS_SENT ->
                showToast(context, context.getString(R.string.mail_is_sent))
            MAIL_IS_DELETED ->
                showToast(context, context.getString(R.string.mail_is_deleted))
//            else ->
//                showToast(context, "Oops, something went wrong")
        }
    }
}

fun showToast(context: Context, message: String){
    Toast.makeText( context, message, Toast.LENGTH_SHORT).show()
}
