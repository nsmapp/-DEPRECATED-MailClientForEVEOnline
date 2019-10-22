package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.usecase.mails.*
import by.nepravskysm.domain.utils.*

class ReadMailViewModel(private val getMailUseCase: GetMailUseCase,
                        private val updataMailMetadataUseCase: UpdataMailMetadataUseCase,
                        private val updateDBMailMetadataUseCase: UpdateDBMailMetadataUseCase,
                        private val deleteMailUseCaseFromServerUseCase: DeleteMailUseCaseFromServerUseCase,
                        private val deleteMailFromDBUseCase: DeleteMailFromDBUseCase) : ViewModel() {

    val inPutMail: MutableLiveData<InPutMail> by lazy { MutableLiveData<InPutMail>() }
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}
    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val mailIsDeleted: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    var subject = ""
    var from = ""
    var fromId: Long = 0
    var mailBody =""
    var mailId: Long = 0


    fun getMail(){

        if (mailBody.equals("")){
            isVisibilityProgressBar.value = true

            getMailUseCase.setMailId(mailId)
            getMailUseCase.execute {
                onComplite {
                    Log.d("logde",  "getMailUseCasecomplite")
                    isVisibilityProgressBar.value = false
                    inPutMail.value = it
                    mailBody = it.body
                    fromId = it.from

                    if(!it.isRead){
                        updataMailMetadataUseCase.setData(mailId, it.labels).execute {
                            onComplite { Log.d("logd",  "UpdateLabel net") }
                            onError { eventId.value = UPDATE_MAIL_METADATA_ERROR }
                        }
                        updateDBMailMetadataUseCase.setData(mailId).execute {
                            onComplite { Log.d("logd",  "UpdateLabel db") }
                            onError { eventId.value = DB_UPDATE_MAIL_METADATA_ERROR }
                        }
                    }


                }
                onError {
                    eventId.value = GET_MAIL_ERROR
                    isVisibilityProgressBar.value = false
                    Log.d("logd", "readMailError ${it.localizedMessage}") }
            }
        }

    }

    fun deleteMail(){
//        mailIsDeleted.value = false
        deleteMailFromDBUseCase.setData(mailId).execute {
            onComplite {  }
            onError { eventId.value = DB_DELETE_MAIL_ERROR }
        }
        deleteMailUseCaseFromServerUseCase.setData(mailId)
        deleteMailUseCaseFromServerUseCase.execute {
            onComplite {
                Log.d("logd", "deletemailcomplite")
                mailIsDeleted.value = true
            }
            onError {
                eventId.value = DELETE_MAIL_FROM_SERVER_ERROR
                Log.d("logd",  "deletemailerror") }

        }
    }


}