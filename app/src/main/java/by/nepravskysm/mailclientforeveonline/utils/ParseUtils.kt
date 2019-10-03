package by.nepravskysm.mailclientforeveonline.utils

import android.os.Build
import android.text.Html
import android.widget.TextView

fun pastHtmlTextToMailBody(view: TextView, htmlText: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        view.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    }else{
        view.text = Html.fromHtml(htmlText)
    }
}