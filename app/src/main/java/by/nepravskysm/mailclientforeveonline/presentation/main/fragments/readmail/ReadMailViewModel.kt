package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.usecase.mails.*
import by.nepravskysm.domain.utils.*

class ReadMailViewModel(
    private val getMails: GetMailUseCase,
    private val updateMailMetadata: UpdataMailMetadataUseCase,
    private val updateDBMailMetadata: UpdateDBMailMetadataUseCase,
    private val deleteMailFromServer: DeleteMailFromServerUseCase,
    private val deleteMailFromDB: DeleteMailFromDBUseCase
) : ViewModel() {

    val inPutMail: MutableLiveData<InPutMail> by lazy { MutableLiveData<InPutMail>() }
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}
    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val mailIsDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    var subject = ""
    var from = ""
    var fromId: Long = 0
    var mailBody =""
    var mailId: Long = 0
    var mailSentTime = ""


    fun getMail(){
        if (mailBody.equals("")){
            isVisibilityProgressBar.value = true

            getMails.setData(mailId)
            getMails.execute {
                onComplite {
                    isVisibilityProgressBar.value = false
                    inPutMail.value = it
                    mailBody = it.body.replace("\n", "<br />")
                    fromId = it.from

                    if(!it.isRead){
                        updateDBMailMetadata.setData(mailId).execute {
                            onComplite { }
                            onError { eventId.value = DB_UPDATE_MAIL_METADATA_ERROR }
                        }
                        updateMailMetadata.setData(mailId, it.labels)
                            .execute {
                            onError { eventId.value = UPDATE_MAIL_METADATA_ERROR }
                        }
                    }
                }
                onError {
                    eventId.value = GET_MAIL_ERROR
                    isVisibilityProgressBar.value = false
                }
            }
        }

    }

    fun deleteMail(){
        deleteMailFromDB.setData(mailId).execute {
            onError { eventId.value = DB_DELETE_MAIL_ERROR }
        }
        deleteMailFromServer.setData(mailId)
            .execute {
                onComplite {
                    if (it) mailIsDeleted.value = true
                    else eventId.value = DELETE_MAIL_FROM_SERVER_ERROR
                }
                onError { eventId.value = DELETE_MAIL_FROM_SERVER_ERROR }
        }
    }


}