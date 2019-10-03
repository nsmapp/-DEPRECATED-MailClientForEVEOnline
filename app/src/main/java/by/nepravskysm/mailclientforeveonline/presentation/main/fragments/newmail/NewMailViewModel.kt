package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail

import android.util.Log
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.OutPutMail
import by.nepravskysm.domain.usecase.mails.SendMailUseCase
import by.nepravskysm.domain.utils.FROM
import by.nepravskysm.domain.utils.MAIL_BODY


class NewMailViewModel(private val sendMailUseCase: SendMailUseCase) : ViewModel()  {



    var mailBody = ""
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
        return OutPutMail(
            0,
            mailBody,
            mutableListOf(),
            subject)

    }

    fun initForwardMail(subject: String, from: String, mailBody: String){

        this.subject ="\nFORWARD: $subject"
        this.mailBody = "\n\n" +
                "FORWARD MAIL\n" +
                "from: $from \n" +
                "subject: $subject \n \n" +
                "$mailBody"

    }

    fun initReplayMail(subject: String, from: String, mailBody: String){
        this.subject ="\nRe: $subject"
        this.mailBody = "\n \n" +
                "REPLAY MAIL\n" +
                "from: $from \n" +
                "subject: $subject \n \n" +
                "$mailBody"

    }


}