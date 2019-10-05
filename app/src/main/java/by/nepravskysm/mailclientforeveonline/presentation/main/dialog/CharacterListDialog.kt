package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import by.nepravskysm.mailclientforeveonline.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_empty.*
import kotlinx.android.synthetic.main.fragment_empty.view.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.os.Build
import android.view.Gravity
import androidx.core.view.marginLeft
import kotlin.math.roundToInt


class CharacterListDialog : DialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog = inflater.inflate(R.layout.fragment_empty, container, false)
        getDialog()!!.setTitle("Choice character")


        for( i in 1..10){
            dialog.root.addView(createItem(dialog.context, 0, "NO_NAME"))
        }
        dialog.root.addView(createItem(dialog.context, 0, "Add new character"))


        return dialog
    }

    private fun createItem(context : Context, characterId: Long, characterName: String): LinearLayout{
        val linearLayout = LinearLayout(context)

        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayout.setOnClickListener { Log.d("logd", "layoutClic $characterName") }

        val imageView = ImageView(context)
        val imageSize = resources.getDimension(R.dimen.image_rectangle_48).toInt()
        imageView.layoutParams = LinearLayout.LayoutParams(
            imageSize,
            imageSize)
        imageView.setPadding(2, 2, 2, 2)
        Picasso.get()
            .load("https://imageserver.eveonline.com/Character/${characterId}_128.jpg")
            .placeholder(R.drawable.no_login_avatar)
            .error(R.drawable.no_login_avatar)
            .into(imageView)

        val textView = TextView(context)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT)
        textView.setPadding(2, 2, 2, 2)
        textView.text = characterName
        textView.gravity = Gravity.CENTER

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.largeTextView)
        }else{
            textView.setTextAppearance(context, R.style.largeTextView)
        }


        linearLayout.addView(imageView)
        linearLayout.addView(textView)

        return  linearLayout
    }
}