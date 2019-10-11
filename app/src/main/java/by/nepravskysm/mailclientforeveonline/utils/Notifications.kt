package by.nepravskysm.mailclientforeveonline.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity

fun makeNotification(message: String, context: Context) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
        channel.description = CHANNEL_DESCRIPTION
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.createNotificationChannel(channel)
    }

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.no_login_avatar)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setVibrate(LongArray(0))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
//        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        .build()

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder)
}
