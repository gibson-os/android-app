package de.wollis_page.gibsonos.helper

import android.util.Log
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.Session
import com.google.android.gms.cast.framework.SessionManagerListener
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import org.json.JSONObject

class Chromecast(val context: GibsonOsActivity): SessionManagerListener<Session> {
    override fun onSessionEnded(p0: Session, p1: Int) {
        Log.d(Config.LOG_TAG, "Session ended")
    }

    override fun onSessionEnding(p0: Session) {
        Log.d(Config.LOG_TAG, "Session ending")
    }

    override fun onSessionResumeFailed(p0: Session, p1: Int) {
        Log.d(Config.LOG_TAG, "Session resume failed")
    }

    override fun onSessionResumed(p0: Session, p1: Boolean) {
        Log.d(Config.LOG_TAG, "Session resumed")
    }

    override fun onSessionResuming(p0: Session, p1: String) {
        Log.d(Config.LOG_TAG, "Session resuming")
    }

    override fun onSessionStartFailed(p0: Session, p1: Int) {
        Log.d(Config.LOG_TAG, "Session start failed")
    }

    override fun onSessionStarted(session: Session, p1: String) {
        Log.d(Config.LOG_TAG, "Session started")

        val castContext = CastContext.getSharedInstance(this.context)
        Log.d(Config.LOG_TAG, castContext.sessionManager.currentCastSession.toString())
        castContext.sessionManager.currentCastSession?.sendMessage(
            "urn:x-cast:net.itronom.gibson",
            JSONObject(mapOf(
                "type" to "user",
                "user" to mapOf(
                    "id" to this.context.getAccount().userId,
                    "user" to this.context.getAccount().userName
                )
            )).toString()
        )
    }

    override fun onSessionStarting(p0: Session) {
        Log.d(Config.LOG_TAG, "Session starting")
    }

    override fun onSessionSuspended(p0: Session, p1: Int) {
        Log.d(Config.LOG_TAG, "Session suspended")
    }
}