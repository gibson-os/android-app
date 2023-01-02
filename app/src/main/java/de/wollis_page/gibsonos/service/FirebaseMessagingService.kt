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
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.helper.Config

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
        val application = this.application as GibsonOsApplication
        val account = remoteMessage.data["token"]?.let { application.getAccountByToken(it) }
        var activity: GibsonOsActivity? = null

        if (account == null) {
            return
        }

        activity = application.getActivity(
            account,
            remoteMessage.data["module"].toString(),
            remoteMessage.data["task"].toString(),
            remoteMessage.data["action"].toString(),
            remoteMessage.data["id"] ?: 0,
        )

        if ((remoteMessage.data["payload"]?.length ?: 0) > 0) {
            // Wenn activity nicht existiert und typ update ist = update abmelden
            activity?.updateData(remoteMessage.data["payload"].toString())
        }

        val intent = application.getActivityIntent(
            account.account,
            remoteMessage.data["module"].toString(),
            remoteMessage.data["task"].toString(),
            remoteMessage.data["action"].toString(),
            remoteMessage.data["id"] ?: 0,
        )

        if (remoteMessage.data["title"] != null) {
            var appIcon = R.drawable.icon

            if (activity is AppActivityInterface) {
                appIcon = activity.getAppIcon()
            }

            val notificationBuilder = NotificationCompat.Builder(this, "test")
                .setSmallIcon(appIcon)
                .setContentTitle(remoteMessage.data["title"])
                .setContentText(remoteMessage.data["body"])
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