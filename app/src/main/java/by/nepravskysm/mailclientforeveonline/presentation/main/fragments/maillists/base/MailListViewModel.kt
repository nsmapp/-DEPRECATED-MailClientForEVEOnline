package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.usecase.mails.GetMailsHeaderUseCase
import java.util.logging.Level

class MailListViewModel(private val getMailsHeaderUseCase: GetMailsHeaderUseCase) : ViewModel() {

    val allMailsHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val sendMailsHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val characterMailsHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val corpMailsHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val allianceMailHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val mailListHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>(false)
    }

    init {
        if(allMailsHeaderList.value!!.isEmpty()) {
            isVisibilityProgressBar.value = true
            getMailHeaderList()
        }

    }

    private fun getMailHeaderList(){
        getMailsHeaderUseCase.execute {
            onComplite {

                allMailsHeaderList.value = it
                sendMailsHeaderList.value = it.filter { mailHeader ->  mailHeader.labels == listOf(2)}
                characterMailsHeaderList.value = it.filter { mailHeader ->  mailHeader.labels == listOf(1)}
                corpMailsHeaderList.value = it.filter { mailHeader ->  mailHeader.labels == listOf(4)}
                allianceMailHeaderList.value = it.filter { mailHeader ->  mailHeader.labels == listOf(8)}
                mailListHeaderList.value = it.filter { mailHeader ->  mailHeader.labels == listOf<MailHeader>()}

                isVisibilityProgressBar.value = false

            }

            onError {

                isVisibilityProgressBar.value = false
                java.util.logging.Logger.getLogger("logdOnError").log(Level.INFO, it.localizedMessage)
            }
        }
    }

}