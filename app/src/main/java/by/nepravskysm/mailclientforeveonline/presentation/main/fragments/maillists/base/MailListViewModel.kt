package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDB
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.domain.usecase.mails.SynchroMailsHeaderUseCase
import by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.recycler.MailRecyclerAdapter
import java.util.logging.Level

class MailListViewModel(private val synchroMailsHeaderUseCase: SynchroMailsHeaderUseCase,
                        private val getMailHeadersFromDB: GetMailHeadersFromDB,
                        private val getNewMailHeadersUseCase: GetNewMailHeadersUseCase) : ViewModel() {



    var allMailsHeaderList = mutableListOf<MailHeader>()
    val mailRecyclerAdapter = MailRecyclerAdapter()

    val sendMailsHeaderList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val inboxHeaderList: MutableLiveData<List<MailHeader>> by lazy {
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

        loadHeadersFromDB()
        loadNewMailHeaders()
    }

    fun loadNewMailHeaders(){
        isVisibilityProgressBar.value = true
        getNewMailHeadersUseCase.execute {
            onComplite {
                isVisibilityProgressBar.value = false
                setAndFilterHeadersList(it)
            }
            onError {
                isVisibilityProgressBar.value = false
                java.util.logging.Logger.getLogger("logdOnError")
                    .log(Level.INFO,"oadNewMailHeaders()  " + it.localizedMessage)
            }
        }
    }


    private fun loadHeadersFromDB() {

        getMailHeadersFromDB.execute {
            onComplite {

                setAndFilterHeadersList(it)
                java.util.logging.Logger.getLogger("logdOnError")
                    .log(Level.INFO, "loadHeadersFromDB() FINISH")
            }

            onError {

                java.util.logging.Logger.getLogger("logdOnError")
                    .log(Level.INFO, it.localizedMessage)

            }

        }
    }

    private fun setAndFilterHeadersList(headersList: List<MailHeader>){

        allMailsHeaderList.addAll(headersList)
        sendMailsHeaderList.value = headersList.filter { mailHeader ->
            mailHeader.labels == listOf(2) || mailHeader.labels == listOf(1, 2)
        }
        inboxHeaderList.value = headersList.filter { mailHeader ->
            mailHeader.labels == listOf(1) || mailHeader.labels == listOf(1, 2)
        }
        corpMailsHeaderList.value =
            headersList.filter { mailHeader -> mailHeader.labels == listOf(4) }
        allianceMailHeaderList.value =
            headersList.filter { mailHeader -> mailHeader.labels == listOf(8) }
        mailListHeaderList.value =
            headersList.filter { mailHeader -> mailHeader.labels == listOf<MailHeader>() }
    }


}