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
    var mailSentTime = ""
    val nameList = mutableSetOf<String>()
    val eventId: MutableLiveData<Long> by lazy { MutableLiveData<Long>() }
    val isVisibilityProgressBar: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    fun sendMail(){
        isVisibilityProgressBar.value = true
        val mail = createOutPutMail()
        sendMail.setData(mail, nameList)
            .execute {
                onComplite {
                    isVisibilityProgressBar.value = false
                    eventId.value = MAIL_IS_SENT
                }
                onError {
                    isVisibilityProgressBar.value = false
                    eventId.value = SEND_MAIL_ERROR
                }
        }
    }

    private fun createOutPutMail(): OutPutMail {
        mailBody += "<br><br> " +
                replyMail.replace("\n", "<br/>")

        return OutPutMail(
            0,
            mailBody,
            mutableListOf(),
            subject)

    }

    fun initForwardMail(
        subject: String,
        from: String,
        replyMailBody: String,
        mailSentTime: String
    ) {

        this.subject = "Fw: $subject"
        this.replyMail =
            "<font size=\"12\" color=\"#bfffffff\">      Sent from: </font>" +
                    "<font size=\"12\" color=\"#ffffa600\"><a href=\"https://play.google.com/store/apps/details?id=by.nepravskysm.mailclientforeveonline\">Mail client for EVE Online</a></font>" +
                    "<font size=\"12\" color=\"#ffb2b2b2\">" +
                    "<br><br>------------------- <br>" +
                    "From: $from <br>" +
                    "Sent: $mailSentTime <br>" +
                    "Subject: $subject<br><br> " +
                    replyMailBody +
                    "</font>"

    }

    fun initReplyMail(subject: String, from: String, replyMailBody: String, mailSentTime: String) {
        this.subject = "Re: $subject"
        this.replyMail =
            "<font size=\"12\" color=\"#bfffffff\">      Sent from: </font>" +
                    "<font size=\"12\" color=\"#ffffa600\"><a href=\"https://play.google.com/store/apps/details?id=by.nepravskysm.mailclientforeveonline\">Mail client for EVE Online</a></font>" +
                    "<font size=\"12\" color=\"#ffb2b2b2\">" +
                    "<br><br>------------------- <br>" +
                    "From: $from <br>" +
                    "Sent: $mailSentTime <br>" +
                    "Subject: $subject<br><br> " +
                    replyMailBody +
                    "</font>"
    }


}