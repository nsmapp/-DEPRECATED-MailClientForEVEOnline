package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.nepravskysm.mailclientforeveonline.BuildConfig
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.utils.BUNDLE_TYPE
import by.nepravskysm.mailclientforeveonline.utils.FEEDBACK
import by.nepravskysm.mailclientforeveonline.utils.FROM
import by.nepravskysm.mailclientforeveonline.utils.SUBJECT
import kotlinx.android.synthetic.main.fragment_about.view.*


class AboutFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        view.versionName.text = "Version " + BuildConfig.VERSION_NAME
        view.ingameContact.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BUNDLE_TYPE, FEEDBACK)
            bundle.putString(FROM, resources.getString(R.string.alisa_red_alert))
            bundle.putString(SUBJECT, resources.getString(R.string.app_feedback))
            findNavController().navigate(R.id.newMailFragment, bundle)
        }
        return view
    }


}