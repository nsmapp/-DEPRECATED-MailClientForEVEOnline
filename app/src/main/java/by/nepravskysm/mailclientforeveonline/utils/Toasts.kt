package by.nepravskysm.mailclientforeveonline.utils

import android.content.Context
import android.widget.Toast

fun showErrorToast(context: Context, errorId: Long){
    Toast.makeText( context, "Text error: $errorId", Toast.LENGTH_SHORT).show()
}

