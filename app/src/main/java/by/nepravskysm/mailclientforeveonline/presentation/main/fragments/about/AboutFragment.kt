package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.nepravskysm.mailclientforeveonline.BuildConfig
import by.nepravskysm.mailclientforeveonline.R
import kotlinx.android.synthetic.main.fragment_about.view.*



class AboutFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.versionName.text = "Version " + BuildConfig.VERSION_NAME
        return view
    }
}