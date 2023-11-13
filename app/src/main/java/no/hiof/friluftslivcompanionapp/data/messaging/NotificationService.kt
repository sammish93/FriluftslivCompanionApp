package no.hiof.friluftslivcompanionapp.data.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import no.hiof.friluftslivcompanionapp.R

// Reference: https://developer.android.com/develop/ui/views/notifications/build-notification
class NotificationService : FirebaseMessagingService() {

    companion object {
        const val CHANNEL_ID = "friluftslivcompanion_notification_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Message Body: ${it.body}")
            showNotification(it)
        }
    }

    private fun showNotification(notification: RemoteMessage.Notification) {
        val notificationBuilder = setNotificationContent(
            R.mipmap.ic_launcher_round,
            notification.title!!,
            notification.body!!
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(CHANNEL_ID,
            "Notification service",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun setNotificationContent(icon: Int, title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

}