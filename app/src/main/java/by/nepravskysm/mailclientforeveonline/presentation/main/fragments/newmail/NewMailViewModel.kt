package by.nepravskysm.mailclientforeveonline.presentation.main.fragments.newmail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.nepravskysm.domain.entity.OutPutMail
import by.nepravskysm.domain.usecase.mails.SendMailUseCase
import by.nepravskysm.domain.utils.MAIL_IS_SENT
import by.nepravskysm.domain.utils.SEND_MAIL_ERROR


class NewMailViewModel(private val sendMail: SendMailUseCase) : ViewModel() {



    var mailBody = ""
    var replyMail = ""
    var subject = ""
    val nameList = mutableSetOf<String>()
    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }


    fun sendMail(){
        val mail = createOutPutMail()
        sendMail.setData(mail, nameList)
            .execute {
                onComplite { eventId.value = MAIL_IS_SENT }
                onError { eventId.value = SEND_MAIL_ERROR }
        }
    }

    private fun createOutPutMail(): OutPutMail {
        mailBody += "<br /><br /> " +
                "------------------- <br />" +
                replyMail.replace("\n", "<br />")

        return OutPutMail(
            0,
            mailBody,
            mutableListOf(),
            subject)

    }

    fun initForwardMail(subject: String, from: String, replyMailBody: String) {

        this.subject = "Fw: $subject"
        this.replyMail =
                "from: $from <br />" +
                "subject: $subject <br /><br /> " +
                        replyMailBody

    }

    fun initReplyMail(subject: String, from: String, replyMailBody: String) {
        this.subject = "Re: $subject"
        this.replyMail =
                "from: $from <br />" +
                "subject: $subject<br /> <br />" +
                        replyMailBody

    }


}