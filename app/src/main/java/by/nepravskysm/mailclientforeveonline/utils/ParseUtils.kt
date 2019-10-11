package by.nepravskysm.mailclientforeveonline.utils

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*

fun pastHtmlTextToMailBody(view: TextView, htmlText: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        view.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT)
    }else{
        view.text = Html.fromHtml(htmlText)
    }
}

fun pastImage(view: ImageView, imageId: Long){
    Picasso.get()
        .load("https://imageserver.eveonline.com/Character/${imageId}_128.jpg")
        .transform(RoundCornerTransform())
        .into(view)
}