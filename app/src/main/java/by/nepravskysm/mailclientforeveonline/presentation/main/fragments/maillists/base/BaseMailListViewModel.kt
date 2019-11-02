package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.maillists.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.MailHeader
import by.nepravskysm.domain.usecase.mails.GetMailHeadersAfterIdFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetMailHeadersFromDBUseCase
import by.nepravskysm.domain.usecase.mails.GetNewMailHeadersUseCase
import by.nepravskysm.domain.usecase.mails.UpdateAllMailMetadataDBUseCase
import by.nepravskysm.domain.utils.DB_LOAD_MAIL_HEADER_FROM_DATABASE
import by.nepravskysm.domain.utils.LOAD_NEW_MAIL_HEADER_ERROR

open class BaseMailListViewModel (
    val getMailHeadersFromDB: GetMailHeadersFromDBUseCase,
    private val getNewMailHeaders: GetNewMailHeadersUseCase,
    val getMailHeadersAfterIdFromDB: GetMailHeadersAfterIdFromDBUseCase,
    private val updateAllMailMetadataDB: UpdateAllMailMetadataDBUseCase
) : ViewModel(){


    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>(false)
    }
    val headerList: MutableLiveData<List<MailHeader>> by lazy {
        MutableLiveData<List<MailHeader>>()
    }

    val addHeaderList: MutableLiveData<List<MailHeader>> by lazy{
        MutableLiveData<List<MailHeader>>(mutableListOf())
    }

    init {
        setMailHeaderLabel()
    }

    open fun setMailHeaderLabel(){}

    fun loadNewMailHeaders(){
        isVisibilityProgressBar.value = true
        getNewMailHeaders.execute {
            onComplite {
                loadHeadersFromDB()
            }
            onError {
                loadHeadersFromDB()
                isVisibilityProgressBar.value = false
                eventId.value = LOAD_NEW_MAIL_HEADER_ERROR
            }
        }
    }

    fun loadHeadersFromDB() {
        isVisibilityProgressBar.value = true
        getMailHeadersFromDB.execute {
            onComplite {
                isVisibilityProgressBar.value = false
                headerList.value = it
            }

            onError {
                isVisibilityProgressBar.value = false
                eventId.value = DB_LOAD_MAIL_HEADER_FROM_DATABASE
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

    fun updateAllMailMetadata() {
        updateAllMailMetadataDB.execute {
            onComplite { loadHeadersFromDB() }
        }
    }

}