package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_read_mail.*
import kotlinx.android.synthetic.main.fragment_read_mail.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ReadMailFragment : Fragment(){

    val fViewModel: ReadMailViewModel by viewModel()


    private val mailObserver = Observer<InPutMail>{ mail ->

        pastHtmlTextToMailBody(body, mail.body)
        pastImage(fromPhoto, mail.from)

    }

    private val progresBarObserver = Observer<Boolean>{
        if(it){showProgresBar()}
        else{hideProgresBar()}
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        val fView = inflater.inflate(R.layout.fragment_read_mail, container, false)
        fView.body.movementMethod = ScrollingMovementMethod()
        fView.rootView.progressBar.visibility = View.GONE

        try {
            pastHtmlTextToMailBody(fView.body, fViewModel.mailBody)
            pastImage(fView.fromPhoto, fViewModel.fromId)
            fViewModel.subject = arguments?.getString(SUBJECT)!!
            fViewModel.from = arguments?.getString(FROM)!!
            fViewModel.mailId = arguments!!.getLong(MAIL_ID)
            fViewModel.inPutMail.observe(this, mailObserver)
        }catch (E: Exception){
            //TODO obrabotat'
        }finally {
            fView.subject.text = fViewModel.subject
            fView.from.text = fViewModel.from
        }

        val navController = NavHostFragment.findNavController(this)



        fViewModel.getMail()
        fViewModel.isVisibilityProgressBar.observe(this, progresBarObserver)
        fViewModel.mailIsDeletedEvent.observer(this, Observer {
            //TODO сделвть красиво

            if(activity != null){
                Toast.makeText(activity, "mail is deleted", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).onBackPressed()
            }

        })



        fView.replayMail
            .setOnClickListener {
                navController.navigate(R.id.newMailFragment, createBundle(REPLAY))
            }

        fView.forwardMail
            .setOnClickListener {
                navController.navigate(R.id.newMailFragment, createBundle(FORWARD))
            }

        fView.deleteMail
            .setOnClickListener {
                fViewModel.deleteMail()
            }

        return fView
    }


    private fun createBundle(bundleType: String) : Bundle{

        val bundle = Bundle()
        bundle.putString(BUNDLE_TYPE, bundleType)
        bundle.putString(FROM, fViewModel.from)
        bundle.putString(SUBJECT, fViewModel.subject)
        bundle.putString(MAIL_BODY, fViewModel.mailBody)

        return bundle
    }

    private fun showProgresBar(){
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgresBar(){
        progressBar.visibility = View.GONE
    }



}