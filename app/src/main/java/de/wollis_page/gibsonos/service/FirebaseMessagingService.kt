package de.wollis_page.gibsonos.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.wollis_page.gibsonos.R
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
        var activity: GibsonOsActivity? = null
        val application = this.application as GibsonOsApplication
        val account = remoteMessage.data["token"]?.let { application.getAccountByToken(it) }
        val activityName = "de.wollis_page.gibsonos.module." +
                remoteMessage.data["module"] + "." +
                remoteMessage.data["task"] + ".activity." +
                (remoteMessage.data["action"].toString().replaceFirstChar { it.uppercase() }) + "Activity"

        Log.d(Config.LOG_TAG, activityName)

        account?.getProcesses()?.forEach {
            Log.d(Config.LOG_TAG, it.activity::class.java.toString())
            if (it.activity::class.java.toString() == "class $activityName") {
                Log.d(Config.LOG_TAG, "--- " + it.activity::class.java.toString())
                activity = it.activity
            }
        }

        if (remoteMessage.notification != null) {
            var intent = Intent(this, Class.forName(activityName))
            intent.putExtra(GibsonOsActivity.ACCOUNT_KEY, account!!.account)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

            if (activity != null) {
                intent = activity!!.intent
            }

            val notificationBuilder = NotificationCompat.Builder(this, "test")
                .setSmallIcon(R.drawable.ic_anchor)
                .setContentTitle(remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
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