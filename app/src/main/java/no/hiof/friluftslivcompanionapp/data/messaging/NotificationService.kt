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

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "New token: $token")

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "From: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            Log.d("FCM", "Notification Message Body: ${it.body}")
            val icon = it.icon
            NotificationHelper.showMessage(this, it.title!!, R.drawable.thunder_storm, it.body!!)
        }
    }
}