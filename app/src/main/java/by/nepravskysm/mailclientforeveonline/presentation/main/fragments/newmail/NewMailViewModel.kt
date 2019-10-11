package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail

import android.util.Log
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.OutPutMail
import by.nepravskysm.domain.usecase.mails.SendMailUseCase


class NewMailViewModel(private val sendMailUseCase: SendMailUseCase) : ViewModel()  {



    var mailBody = ""
    var mailEnd = ""
    var subject = ""
    val nameList = mutableSetOf<String>()


    fun sendMail(){

        val mail = createOutPutMail()

        sendMailUseCase.setData(mail, nameList)
        sendMailUseCase.execute {
            onComplite {  }
            onError { Log.d("logd", "${this.javaClass.name}  ${it.localizedMessage}") }
        }
    }

    private fun createOutPutMail(): OutPutMail {

        mailBody += mailEnd
        return OutPutMail(
            0,
            mailBody,
            mutableListOf(),
            subject)

    }

    fun initForwardMail(subject: String, from: String, mailBody: String){

        this.subject ="\nFORWARD: $subject"
        this.mailEnd = "\n\n" +
                "FORWARD MAIL\n" +
                "from: $from \n" +
                "subject: $subject \n \n" +
                "$mailBody"

    }

    fun initReplayMail(subject: String, from: String, mailBody: String){
        this.subject ="\nRe: $subject"
        this.mailEnd = "\n \n" +
                "REPLAY MAIL\n" +
                "from: $from \n" +
                "subject: $subject \n \n" +
                "$mailBody"

    }


}