package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.usecase.mails.DeleteMailUseCase
import by.nepravskysm.domain.usecase.mails.GetMailUseCase
import by.nepravskysm.domain.usecase.mails.UpdataMailMetadataUseCase
import by.nepravskysm.mailclientforeveonline.utils.SingleLiveEvent

class ReadMailViewModel(private val getMailUseCase: GetMailUseCase,
                        private val updataMailMetadataUseCase: UpdataMailMetadataUseCase,
                        private val deleteMailUseCase: DeleteMailUseCase) : ViewModel() {

    val inPutMail: MutableLiveData<InPutMail> by lazy { MutableLiveData<InPutMail>() }
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}
    val mailIsDeletedEvent = SingleLiveEvent<Any>()

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
                        updataMailMetadataUseCase.setData(mailId, it.labels)
                        updataMailMetadataUseCase.execute {  }
                    }


                }
                onError {
                    isVisibilityProgressBar.value = false
                    Log.d("logd", "readMailError ${it.localizedMessage}") }
            }
        }

    }

    fun deleteMail(){
        deleteMailUseCase.setData(mailId)
        deleteMailUseCase.execute {
            onComplite { Log.d("logd",  "deletemailcomplite")
            mailIsDeletedEvent.call()}

            onError { Log.d("logd",  "deletemailerror") }
        }
    }

}