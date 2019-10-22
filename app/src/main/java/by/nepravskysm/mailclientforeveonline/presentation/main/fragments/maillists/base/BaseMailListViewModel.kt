package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.usecase.mails.GetMailHeadersAfterIdFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.domain.utils.DB_LOAD_MAIL_HEADER_FROM_DATABASE
import by.nepravskysm.domain.utils.LOAD_NEW_MAIL_HEADER_ERROR
import java.util.logging.Level

open class BaseMailListViewModel (
    val getMailHeadersFromDB: GetMailHeadersFromDBUseCase,
    private val getNewMailHeaders: GetNewMailHeadersUseCase,
    val getMailHeadersAfterIdFromDB: GetMailHeadersAfterIdFromDBUseCase
) : ViewModel(){


    var mailHeaderList = mutableListOf<MailHeader>()


    val unreadMailCount: MutableLiveData<Int> by lazy {MutableLiveData<Int>(0)}
    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>(false)
    }
    val headerList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    val addHeaderList: MutableLiveData<List<MailHeader>> by lazy{
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    init {
        setMailHeaderLabel()
        loadHeadersFromDB()
    }

    open fun setMailHeaderLabel(){}

    fun loadNewMailHeaders(){
        isVisibilityProgressBar.value = true
        getNewMailHeaders.execute {
            onComplite {
                loadHeadersFromDB()
            }
            onError {
                isVisibilityProgressBar.value = false
                eventId.value = LOAD_NEW_MAIL_HEADER_ERROR
                java.util.logging.Logger.getLogger("logdOnError")
                    .log(Level.INFO,"oadNewMailHeaders()  " + it.localizedMessage)
            }
        }
    }

    fun loadHeadersFromDB() {
        isVisibilityProgressBar.value = true
        getMailHeadersFromDB.execute {
            onComplite {
                isVisibilityProgressBar.value = false
                headerList.value = it
                calcUnreadMail(it)
            }

            onError {
                isVisibilityProgressBar.value = false
                eventId.value = DB_LOAD_MAIL_HEADER_FROM_DATABASE
                java.util.logging.Logger.getLogger("logdOnError")
                    .log(Level.INFO, it.localizedMessage)

            }

        }
    }

    fun loadHeadersAfterId(lastHeaderId: Long){
        isVisibilityProgressBar.value = true
        getMailHeadersAfterIdFromDB.setData(lastHeaderId).execute {
            onComplite {
                addHeaderList.value = it
                isVisibilityProgressBar.value = false

            }
            onError {
                isVisibilityProgressBar.value = false
            }
        }
    }

    private fun calcUnreadMail(headersList: List<MailHeader>) {
        unreadMailCount.value = headersList.filter { header -> !header.isRead }.size
    }

}