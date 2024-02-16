package de.wollis_page.gibsonos.module.explorer.service

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.explorer.task.Html5Task
import org.json.JSONObject

class ChromecastService(private val context: GibsonOsActivity) {
    var castContext: CastContext? = null
    var sessionManager: SessionManager? = null
    var castSession: CastSession? = null
    var mediaRouteMenuItem: MenuItem? = null
    private val sessionManagerListener: SessionManagerListener<CastSession> =
        SessionManagerListenerImpl()

    init {
        // If without context the button will not rendered on startup
        this.castContext = CastContext.getSharedInstance(this.context)
        this.sessionManager = this.castContext?.sessionManager
//        this.sessionManager?.addSessionManagerListener(sessionManagerListener, CastSession::class.java)
    }

    fun onResume() {
        this.castSession = this.sessionManager?.currentCastSession
        this.sessionManager?.addSessionManagerListener(this.sessionManagerListener, CastSession::class.java)
    }

    fun onPause() {
        this.sessionManager?.removeSessionManagerListener(this.sessionManagerListener, CastSession::class.java)
        this.castSession = null
    }

    private inner class SessionManagerListenerImpl: SessionManagerListener<CastSession> {
        override fun onSessionStarting(session: CastSession) {
        }

        override fun onSessionStarted(session: CastSession, sessionId: String) {
            resumeSession(session)
        }

        override fun onSessionStartFailed(session: CastSession, error: Int) {
            releaseSession()
            val castReasonCode = castContext?.getCastReasonCodeForCastStatusCode(error)
            // Handle error
        }

        override fun onSessionSuspended(session: CastSession, reason: Int) {
            releaseSession()
        }

        override fun onSessionResuming(session: CastSession, sessionId: String) {
        }

        override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
            resumeSession(session)
        }

        override fun onSessionResumeFailed(session: CastSession, error: Int) {
            releaseSession()
        }

        override fun onSessionEnding(session: CastSession) {
        }

        override fun onSessionEnded(session: CastSession, error: Int) {
            releaseSession()
//            context.finish()
        }
    }

    private fun resumeSession(session: CastSession) {
        Log.d(Config.LOG_TAG, "Start chromecast session")
        val sessionId = session.sessionId

        if (sessionId === null) {
            return
        }

        this.context.runTask({
            Html5Task.setSession(this.context, sessionId)
            this.castSession = session

            this.context.runOnUiThread {
                val userMessage = JSONObject(mapOf(
                    "type" to "user",
                    "user" to mapOf(
                        "id" to this.context.getAccount().userId,
                        "user" to this.context.getAccount().userName,
                    )
                ))
                session.sendMessage(
                    "urn:x-cast:net.itronom.gibson",
                    userMessage.toString()
                )
                this.context.invalidateOptionsMenu()
            }
        })
    }

    private fun releaseSession() {
        Log.d(Config.LOG_TAG, "Release chromecast session")
        this.castSession = null
    }

    fun onCreateOptionsMenu(menu: Menu) {
        this.context.menuInflater.inflate(R.menu.explorer_index_menu, menu)
        this.mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(
            this.context.applicationContext,
            menu,
            R.id.media_route_menu_item
        )
    }

    fun showMediaRouterDialog() {
//        val menuItem = this.context.findViewById(R.id.media_route_menu_item) as MenuItem
//        this.context.onOptionsItemSelected(menuItem);
    }
}