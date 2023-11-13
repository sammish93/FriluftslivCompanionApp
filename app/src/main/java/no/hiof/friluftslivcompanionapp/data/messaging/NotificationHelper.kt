package no.hiof.friluftslivcompanionapp.data.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import no.hiof.friluftslivcompanionapp.R

// Reference: https://developer.android.com/develop/ui/views/notifications/build-notification
object NotificationHelper {

    private const val CHANNEL_ID = "friluftslivcompanion_notification_channel"
    private const val NOTIFICATION_ID = 1

    fun showMessage(context: Context, title: String, icon: Int, body: String) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Notification Service",
            NotificationManager.IMPORTANCE_HIGH
        )

        manager.createNotificationChannel(channel)
        manager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }
}