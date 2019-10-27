package by.nepravskysm.mailclientforeveonline.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import by.nepravskysm.domain.usecase.mails.GetNewMailCountUseCase
import by.nepravskysm.domain.usecase.mails.GetUnreadMailUseCase
import by.nepravskysm.mailclientforeveonline.utils.makeNotification
import org.koin.core.KoinComponent
import org.koin.core.inject

class CheckNewMailWorker(context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams), KoinComponent{

    private val getNewMailHeaders: GetNewMailCountUseCase by inject()
    private val getUnreadMail: GetUnreadMailUseCase by inject()
    private val ctx:Context = context

    override fun doWork(): Result {
        var result = Result.success()

        getNewMailHeaders.execute {
            onComplite {
                getUnreadMail.execute {
                    onComplite {
                        if (it.getAllUnreadMailsCount() != 0) {
                            makeNotification(
                                "You have ${it.getAllUnreadMailsCount()} " +
                                        "unread mail", ctx
                            )
                        }
                    }
                }
            }
        }
        return result
    }
}