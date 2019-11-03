package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.settings

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.utils.DARK_MODE
import by.nepravskysm.mailclientforeveonline.utils.NOTISICATION_SOUND
import by.nepravskysm.mailclientforeveonline.utils.SETTINGS
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment(){

    lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_settings, container,false)
        pref = (activity as MainActivity).getSharedPreferences(SETTINGS, MODE_PRIVATE)
        val editor = pref.edit()

        view.darkMode.isChecked = pref.getBoolean(DARK_MODE, false)

        view.darkMode.setOnCheckedChangeListener{ _, _ ->
            when(view.darkMode.isChecked){
                true -> {editor.putBoolean(DARK_MODE, true)
                    editor.apply()
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)}
                false ->{ editor.putBoolean(DARK_MODE, false)
                    editor.apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)}
            }
        }

        view.notificationSound.isChecked = pref.getBoolean(NOTISICATION_SOUND, true)
        view.notificationSound.setOnClickListener {
            when (view.notificationSound.isChecked) {
                true -> {
                    editor.putBoolean(NOTISICATION_SOUND, true)
                    editor.apply()
                }
                false -> {
                    editor.putBoolean(NOTISICATION_SOUND, false)
                    editor.apply()
                }

            }
        }
        return view
    }
}