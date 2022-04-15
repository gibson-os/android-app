package de.wollis_page.gibsonos.helper

import android.util.Log
import com.google.android.gms.cast.framework.Session
import com.google.android.gms.cast.framework.SessionManagerListener

class Chromecast: SessionManagerListener<Session> {
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

    override fun onSessionStarted(p0: Session, p1: String) {
        Log.d(Config.LOG_TAG, "Session started")
    }

    override fun onSessionStarting(p0: Session) {
        Log.d(Config.LOG_TAG, "Session starting")
    }

    override fun onSessionSuspended(p0: Session, p1: Int) {
        Log.d(Config.LOG_TAG, "Session suspended")
    }
}