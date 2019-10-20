package by.nepravskysm.mailclientforeveonline.utils

import android.content.Context
import android.widget.Toast
import by.nepravskysm.domain.utils.*

fun makeToastMessage(context: Context, messageId: Long){

    when(messageId){
        AUTH_ERROR ->
            showToast(context, "Authorization error")
        SYNCHRONIZE_CONTACT_ERROR ->
            showToast(context, "Unable to sync contacts")
        SYNCHRONIZE_MAIL_HEADER_ERROR ->
            showToast(context, "Mail synchronization error")
        LOAD_NEW_MAIL_HEADER_ERROR ->
            showToast(context,"Mail synchronization error")
        SEND_MAIL_ERROR ->
            showToast(context, "Failed to send email")
        GET_MAIL_ERROR ->
            showToast(context, "Failed to receive email")
        UPDATE_MAIL_METADATA_ERROR ->
            showToast(context, "Failed to update mail metadata")
        DB_DELETE_MAIL_ERROR ->
            showToast(context,"Failed to delete email.")
        else ->
            showToast(context, "Oops, something went wrong")

    }

}

fun showToast(context: Context, message: String){
    Toast.makeText( context, message, Toast.LENGTH_SHORT).show()
}
