package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity
import by.nepravskysm.mailclientforeveonline.utils.*
import kotlinx.android.synthetic.main.fragment_read_mail.*
import kotlinx.android.synthetic.main.fragment_read_mail.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class ReadMailFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    val fViewModel: ReadMailViewModel by viewModel()


    private val mailObserver = Observer<InPutMail>{ mail ->
        pastHtmlTextToMailBody(body, mail.body)
        pastImage(fromPhoto, mail.from)
    }
    private val eventIdObserver =
        Observer<Long> { eventId -> makeToastMessage((activity as MainActivity), eventId) }
    private val deleteMailObserver = Observer<Boolean> {
        if (it) {
//            makeToastMessage((activity as MainActivity), MAIL_IS_DELETED)
            findNavController().popBackStack()
        }  }
    private val progresBarObserver = Observer<Boolean> { swipeRefresh.isRefreshing = it }



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
            fViewModel.mailSentTime = arguments?.getString(MAIL_SENT_TIME)!!
            fViewModel.inPutMail.observe(this, mailObserver)
        }catch (E: Exception){
        }finally {
            fView.subject.text = fViewModel.subject
            fView.from.text = fViewModel.from
            fView.time.text = fViewModel.mailSentTime
        }

        val navController = NavHostFragment.findNavController(this)
        fViewModel.getMail()
        fViewModel.isVisibilityProgressBar.observe(viewLifecycleOwner, progresBarObserver)
        fViewModel.mailIsDeleted.observe(viewLifecycleOwner, deleteMailObserver)
        fViewModel.eventId.observe(viewLifecycleOwner, eventIdObserver)
        fView.swipeRefresh.setOnRefreshListener(this)

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
        bundle.putString(REPLY_MAIL_BODY, fViewModel.mailBody)
        bundle.putString(MAIL_SENT_TIME, fViewModel.mailSentTime)

        return bundle
    }

    override fun onRefresh() {
        fViewModel.getMail()
    }



}