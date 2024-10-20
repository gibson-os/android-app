package de.wollis_page.gibsonos.module.explorer.html5.activity

//import android.widget.MediaController
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.explorer.html5.dialog.PlayDialog
import de.wollis_page.gibsonos.module.explorer.html5.dto.Position
import de.wollis_page.gibsonos.module.explorer.index.dto.Media
import de.wollis_page.gibsonos.module.explorer.service.PlaybackService
import de.wollis_page.gibsonos.module.explorer.task.Html5Task
import de.wollis_page.gibsonos.service.AppIntentExtraService

class PlayerActivity: GibsonOsActivity() {
    private lateinit var media: Media
//    private lateinit var videoView: VideoView
    private lateinit var mediaController: MediaController
    private var pausePosition: Int = 0
    private var isPlayingBeforePause: Boolean = false

    override fun getContentView(): Int = R.layout.explorer_html5_player

    override fun getId(): Any = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.removeHeader()

        this.media = AppIntentExtraService.getIntentExtra("media", this.intent) as Media
        var cleanUrl = this.getAccount().url

        if (!cleanUrl.endsWith("/")) {
            cleanUrl += '/'
        }

        if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
            cleanUrl = "http://$cleanUrl"
        }

        cleanUrl += "explorer/html5/stream/token/" + this.media.token

        val sessionToken = SessionToken(this, ComponentName(this, PlaybackService::class.java))
        val controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())

        this.mediaController = controllerFuture.get()

        val mediaItem =
            MediaItem.Builder()
                .setMediaId(this.media.token ?: "")
                .setUri(Uri.parse(cleanUrl))
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(this.media.name)
//                        .setArtworkUri(this.media.)
                        .build()
                )
                .build()

        mediaController.setMediaItem(mediaItem)
        mediaController.prepare()
        mediaController.play()

//        this.videoView = findViewById<VideoView>(R.id.video)
//        val mediaController = MediaController(this)
//        mediaController.setAnchorView(this.videoView)
//        this.videoView.setMediaController(controllerFuture)
//        this.videoView.setVideoURI(Uri.parse(cleanUrl), mapOf("X-Device-Token" to this.getAccount().token))

        this.runTask({
            var position: Position? = null

            try {
                position = Html5Task.getPosition(this, this.media.token!!)
            } catch (_: TaskException) {
            }

            this.runOnUiThread {
                if ((position?.position ?: 0) > 0) {
                    PlayDialog(this).build(
                        media.duration,
                        position,
                        {
                            this.startVideo()
                        },
                        {
                            this.mediaController.seekTo(((position?.position ?: 0) * 1000).toLong())
//                            videoView.seekTo((position?.position ?: 0) * 1000)
                            this.startVideo()
                        },
                    ).show()
                } else {
                    this.startVideo()
                }
            }
        })

//        this.videoView.setOnClickListener {
//            if (this.videoView.isPlaying) {
//                this.videoView.pause()
//
//                return@setOnClickListener
//            }
//
//            this.videoView.start()
//        }

//        this.videoView.setOnCompletionListener {
//            this.setResult(this.media.token.toString(), media.position ?: 0)
//            this.finish()
//        }
    }

    fun startVideo() {
        this.mediaController.play()
//        this.videoView.start()

        this.runTask({
            this.savePosition(this.media.token.toString(), media.position ?: 0)
        })
    }

    private fun savePosition(token: String, lastPosition: Int) {
        var newPosition = lastPosition

//        if (this.videoView.currentPosition != lastPosition) {
//            newPosition = this.videoView.currentPosition / 1000
//
//            if (newPosition > 0) {
//                try {
//                    Html5Task.savePosition(this, token, newPosition)
//                    this.media.position = newPosition
//
//                    this.runOnUiThread {
//                        this.setResult(token, newPosition)
//                    }
//                } catch (_: TaskException) {
//                }
//            }
//        }

        Thread.sleep(1000)
        this.savePosition(token, newPosition)
    }

    private fun setResult(token: String, position: Int) {
        val result = Intent()
        result.putExtra("token", token)
        result.putExtra("position", position)
        this.setResult(RESULT_OK, result)
    }

    override fun onPause() {
//        this.pausePosition = this.videoView.currentPosition
//        this.isPlayingBeforePause = this.videoView.isPlaying
//        this.videoView.pause()

        super.onPause()
    }

    override fun onResume() {
//        this.videoView.seekTo(this.pausePosition)
//
//        if (this.isPlayingBeforePause) {
//            this.videoView.start()
//        }

        super.onResume()
    }
}