package de.wollis_page.gibsonos.helper

import android.content.Context
import com.google.android.gms.cast.framework.CastOptions
import com.google.android.gms.cast.framework.OptionsProvider
import com.google.android.gms.cast.framework.SessionProvider

class CastOptionsProvider : OptionsProvider {
    companion object {
        const val CUSTOM_NAMESPACE = "urn:x-cast:net.itronom.gibson"
    }

    override fun getCastOptions(context: Context): CastOptions {
        val supportedNamespaces: MutableList<String> = ArrayList()
        supportedNamespaces.add(CUSTOM_NAMESPACE)

        return CastOptions.Builder()
            .setReceiverApplicationId(Config.CHROMECAST_RECEIVER_APPLICATION_ID)
            .setSupportedNamespaces(supportedNamespaces)
            .build()
    }

    override fun getAdditionalSessionProviders(p0: Context): MutableList<SessionProvider>? {
        return null
    }
}