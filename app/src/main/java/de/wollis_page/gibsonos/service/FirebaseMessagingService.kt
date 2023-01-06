package de.wollis_page.gibsonos.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(Config.LOG_TAG, "Refreshed firebase token: $token")

        val application = this.application as GibsonOsApplication
        application.firebaseToken = token

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(Config.LOG_TAG, "Firebase message received!")
        Log.d(Config.LOG_TAG, remoteMessage.data.toString())
        val application = this.application as GibsonOsApplication
        val account = remoteMessage.data["token"]?.let { application.getAccountByToken(it) }
            ?: return

        val now = LocalDateTime.now()
        val message = Message(
            account.account,
            remoteMessage.data["module"].toString(),
            remoteMessage.data["task"].toString(),
            remoteMessage.data["action"].toString(),
            remoteMessage.data["title"].toString(),
            remoteMessage.data["body"].toString(),
            now.format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + now.format(DateTimeFormatter.ISO_LOCAL_TIME),
            remoteMessage.data["payload"].toString(),
        )

        if (remoteMessage.data["title"] != null) {
            message.save()

            val intent = application.getActivityIntent(
                account.account,
                message.module,
                message.task,
                message.action,
                remoteMessage.data["id"] ?: 0,
            )

            AppIntentExtraService.putExtras(message, intent)

            val notificationBuilder = NotificationCompat.Builder(this, "test")
                .setSmallIcon(AppIconService.getIcon(
                    message.module,
                    message.task,
                    message.action,
                ) ?: R.drawable.icon)
                .setContentTitle(message.title)
                .setContentText(message.body)
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setAutoCancel(true)
//                .setSound(defaultSoundUri)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("test",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
        }
    }
}