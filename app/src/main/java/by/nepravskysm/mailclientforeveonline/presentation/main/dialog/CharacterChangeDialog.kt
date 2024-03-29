package by.nepravskysm.mailclientforeveonline.presentation.main.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import by.nepravskysm.domain.usecase.auth.GetAllCharactersAuthInfoUseCase
import by.nepravskysm.domain.usecase.character.ChangeActiveCharacter
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.utils.RoundCornerTransform
import by.nepravskysm.rest.api.createAuthUrl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_empty.view.*
import org.koin.android.ext.android.inject


class CharacterChangeDialog : DialogFragment(){

    private val getAllCharactersAuthInfo: GetAllCharactersAuthInfoUseCase by inject()
    private val changeActiveCharacter: ChangeActiveCharacter by inject()
    private var changeCharacterListener: ChangeCharacterListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog = inflater.inflate(R.layout.fragment_empty, container, false)
        getDialog()!!.setTitle("Choice character")


        getAllCharactersAuthInfo.execute {
            onComplite {
                if (it.isEmpty()) {
                }
                for( character in it){
                    dialog.root.addView(
                        createItem(dialog.context,
                            character.characterId,
                            character.characterName))
                }
            }
        }
        dialog.root.addView(createItem(dialog.context, 0, "Add new character"))

        return dialog
    }

    fun setChangeCharacterListener(changeCharacterListener: ChangeCharacterListener){
        this.changeCharacterListener = changeCharacterListener
    }

    private fun createItem(context : Context, characterId: Long, characterName: String): LinearLayout{
        val linearLayout = LinearLayout(context)

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
            .transform(RoundCornerTransform(20f))
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

        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayout.setOnClickListener {

            if (characterName.equals(getString(R.string.add_new_character))) {
                val url: String = createAuthUrl()
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }else{
                changeActiveCharacter
                    .setData(textView.text.toString())
                    .execute {
                        onComplite {
                            if(changeCharacterListener != null){
                                changeCharacterListener?.characterChanged()
                                findNavController().navigate(R.id.inboxFragment)
                            }
                        }
                    }
                this.dismiss()
            }
        }

        linearLayout.addView(imageView)
        linearLayout.addView(textView)

        return  linearLayout
    }

    interface ChangeCharacterListener{
        fun characterChanged()
    }
}