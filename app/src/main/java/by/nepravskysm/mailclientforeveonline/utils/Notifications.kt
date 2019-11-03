package by.nepravskysm.mailclientforeveonline.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import by.nepravskysm.mailclientforeveonline.R
import by.nepravskysm.mailclientforeveonline.presentation.main.MainActivity

fun makeNotification(message: String, context: Context) {

    val pref = context.getSharedPreferences(SETTINGS, AppCompatActivity.MODE_PRIVATE)
    val isSound = pref.getBoolean(NOTISICATION_SOUND, true)
    val sound = setNotificationVolome(isSound)

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
        .setSmallIcon(R.drawable.ic_mail)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText(message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setDefaults(sound)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()


    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder)
}

private fun setNotificationVolome(isSound: Boolean): Int {
    if (isSound) {
        return NotificationCompat.DEFAULT_SOUND
    }

    return NotificationCompat.DEFAULT_LIGHTS
}
