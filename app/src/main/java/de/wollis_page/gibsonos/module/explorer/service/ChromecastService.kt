package de.wollis_page.gibsonos.module.explorer.service

import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class ChromecastService(private val context: GibsonOsActivity) {
    var castContext: CastContext? = null
    var sessionManager: SessionManager? = null
    private var castSession: CastSession? = null
    private val sessionManagerListener: SessionManagerListener<CastSession> =
        SessionManagerListenerImpl()

    init {
        // If without context the button will not rendered on startup
        this.castContext = CastContext.getSharedInstance(this.context)
    }

    private inner class SessionManagerListenerImpl: SessionManagerListener<CastSession> {
        override fun onSessionStarting(session: CastSession) {}

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            context.invalidateOptionsMenu()
        }

        override fun onSessionStartFailed(session: CastSession, error: Int) {
            val castReasonCode = castContext?.getCastReasonCodeForCastStatusCode(error)
            // Handle error
        }

        override fun onSessionSuspended(session: CastSession, reason: Int) {}

        override fun onSessionResuming(session: CastSession, sessionId: String) {}

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            context.invalidateOptionsMenu()
        }

        override fun onSessionResumeFailed(session: CastSession, error: Int) {}

        override fun onSessionEnding(session: CastSession) {}

        override fun onSessionEnded(session: CastSession, error: Int) {
//            context.finish()
        }
    }

    fun onResume() {
        this.castSession = this.sessionManager?.currentCastSession
        this.sessionManager?.addSessionManagerListener(this.sessionManagerListener, CastSession::class.java)
    }

    fun onPause() {
        this.sessionManager?.removeSessionManagerListener(this.sessionManagerListener, CastSession::class.java)
        this.castSession = null
    }
}