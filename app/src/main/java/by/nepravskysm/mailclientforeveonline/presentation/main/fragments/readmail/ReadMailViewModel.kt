package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.readmail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.InPutMail
import by.nepravskysm.domain.usecase.mails.GetMailUseCase

class ReadMailViewModel(private val getMailUseCase: GetMailUseCase) : ViewModel() {

    val inPutMail: MutableLiveData<InPutMail> by lazy { MutableLiveData<InPutMail>() }

    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy{MutableLiveData<Boolean>(false)}

    var subject = ""
    var from = ""
    var fromId: Long = 0
    var mailBody =""


    fun getMail(mailId: Long){

        if (mailBody.equals("")){
            isVisibilityProgressBar.value = true

            getMailUseCase.setMailId(mailId)
            getMailUseCase.execute {
                onComplite {
                    isVisibilityProgressBar.value = false
                    inPutMail.value = it
                    mailBody = it.body
                    fromId = it.from

                }
                onError {
                    isVisibilityProgressBar.value = false
                    Log.d("logd", "readMailError ${it.localizedMessage}") }
            }
        }

    }


}