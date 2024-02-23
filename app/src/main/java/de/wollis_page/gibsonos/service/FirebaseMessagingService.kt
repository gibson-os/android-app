package de.wollis_page.gibsonos.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Message
import de.wollis_page.gibsonos.module.core.task.UserTask
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


open class FirebaseMessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d(Config.LOG_TAG, "Refreshed firebase token: $token")

        val application = this.application as GibsonOsApplication
        application.firebaseToken = token

        application.accounts.forEach {
            UserTask.updateFcmToken(it.account, token)
        }
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
            remoteMessage.data["vibrate"].toString(),
            remoteMessage.data["sound"].toString(),
        )

        if (!remoteMessage.data["title"].isNullOrEmpty()) {
            message.save()

            val intent = application.getActivityIntent(
                account.account,
                message.module,
                message.task,
                message.action,
            )

            AppIntentExtraService.putExtras(message, intent)

            val adapter = Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(LongArray::class.java)
            val vibrate = adapter.fromJson(message.vibrate) ?: longArrayOf()
            val notificationBuilder = NotificationCompat.Builder(this, "gos")
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
                .setVibrate(vibrate)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel("gos",
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)

            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(VibrationEffect.createWaveform(vibrate, -1))

            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }
}