package de.wollis_page.gibsonos.helper

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider
import com.google.android.gms.cast.framework.media.CastMediaOptions
import com.google.android.gms.cast.framework.media.NotificationOptions
import de.wollis_page.gibsonos.module.explorer.html5.activity.ChromecastActivity

class CastOptionsProvider : OptionsProvider {
    override fun getCastOptions(context: Context): CastOptions {
        val notificationOptions = NotificationOptions.Builder()
            .setTargetActivityClassName(ChromecastActivity::class.java.name)
            .build()
        val mediaOptions = CastMediaOptions.Builder()
            .setNotificationOptions(notificationOptions)
            .setExpandedControllerActivityClassName(ChromecastActivity::class.java.name)
            .build()

        return CastOptions.Builder()
            .setReceiverApplicationId(Config.CHROMECAST_RECEIVER_APPLICATION_ID)
            .setCastMediaOptions(mediaOptions)
            .build()
    }

    override fun getAdditionalSessionProviders(p0: Context): MutableList<SessionProvider>? {
        return null
    }
}