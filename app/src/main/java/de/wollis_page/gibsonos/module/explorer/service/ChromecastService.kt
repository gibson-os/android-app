package de.wollis_page.gibsonos.module.explorer.service

import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaLoadOptions
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.google.android.gms.common.images.WebImage
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.explorer.html5.dialog.PlayDialog
import de.wollis_page.gibsonos.module.explorer.html5.dialog.QueueDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.Html5Task
import org.json.JSONObject


class ChromecastService(
    val context: GibsonOsActivity,
    private var updatePositionCallback: ((castSession: CastSession?) -> Unit)? = null,
) {
    var castContext: CastContext? = null
    private var sessionManager: SessionManager? = null
    var castSession: CastSession? = null
    private var mediaRouteMenuItem: MenuItem? = null
    private val miniControllerView: View
    private val sessionManagerListener: SessionManagerListener<CastSession> =
        SessionManagerListenerImpl()
    private var isPlaying = false

    init {
        // If without context the button will not rendered on startup
        this.castContext = CastContext.getSharedInstance(this.context)
        this.sessionManager = this.castContext?.sessionManager
        this.miniControllerView = this.context.findViewById(R.id.castMiniController)
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
        }
    }

    private fun resumeSession(session: CastSession) {
        Log.d(Config.LOG_TAG, "Start chromecast session")
        val sessionId = session.sessionId
        Log.d(Config.LOG_TAG, "sessionId: " +  sessionId.toString())

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

                session.remoteMediaClient?.registerCallback(object : RemoteMediaClient.Callback() {
                    override fun onStatusUpdated() {
                        if (isPlaying == session.remoteMediaClient?.isPlaying) {
                            return
                        }

                        isPlaying = session.remoteMediaClient?.isPlaying ?: false
                        updatePosition()
                    }
                })

                this.isPlaying = session.remoteMediaClient?.isPlaying ?: false
                this.updatePosition()
                this.context.invalidateOptionsMenu()
            }
        })
    }

    private fun updatePosition() {
        this.context.runTask({
            while (isPlaying) {
                this.context.runOnUiThread {
                    this.updatePositionCallback?.invoke(this.castSession)
                }

                Thread.sleep(1000)
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

    fun loadMedia(item: Item) {
        val duration = item.metaInfos?.get("duration").toString().toFloat()
        val position = item.position ?: 0
        val mediaInfo = this.buildMediaInfo(item)
        val remoteMediaClient = this.castSession?.remoteMediaClient

        if (remoteMediaClient?.isPlaying == true || remoteMediaClient?.isPaused == true) {
            QueueDialog(this).build(
                mediaInfo,
                duration.toLong(),
                position.toLong(),
            ).show()

            return
        }

        if (position == 0) {
            this.playMedia(mediaInfo)

            return
        }

        PlayDialog(this.context).build(
            duration.toInt(),
            position,
            {
                this.playMedia(mediaInfo)
            },
            {
                this.playMedia(
                    mediaInfo,
                    (item.position.toString().toFloat() * 1000).toLong()
                )
            },
        ).show()
    }

    fun playMedia(mediaInfo: MediaInfo, position: Long = 0) {
        this.castSession?.remoteMediaClient?.load(
            mediaInfo,
            MediaLoadOptions.Builder().setAutoplay(true).setPlayPosition(position).build()
        )
    }

    fun addMedia(mediaInfo: MediaInfo) {
        this.castSession?.remoteMediaClient?.queueAppendItem(
            MediaQueueItem.Builder(mediaInfo).setAutoplay(true).build(),
            JSONObject()
        )
    }

    private fun buildMediaInfo(item: Item): MediaInfo {
        val name = item.name
        val token = item.html5VideoToken.toString()
        val duration = (item.metaInfos?.get("duration").toString().toFloat() * 1000).toLong()
        var mediaType = MediaMetadata.MEDIA_TYPE_MOVIE
        var streamType = MediaInfo.STREAM_TYPE_BUFFERED
        var contentType = "video/mp4"

        if (item.category == 4) {
            mediaType = MediaMetadata.MEDIA_TYPE_MUSIC_TRACK
            streamType = MediaInfo.STREAM_TYPE_NONE
            contentType = "audio/mpeg"
        }

        val movieMetadata = MediaMetadata(mediaType)
        movieMetadata.putString(MediaMetadata.KEY_TITLE, name)
        var url = this.context.getAccount().url

        if (!url.endsWith("/")) {
            url += '/'
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }

        movieMetadata.addImage(WebImage(Uri.parse(
            "${url}explorer/html5/image/token/$token/deviceToken/${this.context.getAccount().token}/height/800/image.jpg"
        )))

        return MediaInfo.Builder(token)
            .setStreamType(streamType)
            .setContentType(contentType)
            .setMetadata(movieMetadata)
            .setStreamDuration(duration * 1000)
            .setContentUrl(
                "/middleware/chromecast/stream/token/" + token + "?id=" + this.castSession?.sessionId
            )
            .build()
    }
}