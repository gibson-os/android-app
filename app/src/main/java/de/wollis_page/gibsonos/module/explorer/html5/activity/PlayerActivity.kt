package de.wollis_page.gibsonos.module.explorer.html5.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.module.explorer.html5.dialog.PlayDialog
import de.wollis_page.gibsonos.module.explorer.html5.dto.Position
import de.wollis_page.gibsonos.module.explorer.index.dto.Media
import de.wollis_page.gibsonos.module.explorer.task.Html5Task
import de.wollis_page.gibsonos.service.AppIntentExtraService

class PlayerActivity: GibsonOsActivity() {
    private lateinit var media: Media

    override fun getContentView(): Int = R.layout.explorer_html5_player

    override fun getId(): Any = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.removeHeder()

        this.media = AppIntentExtraService.getIntentExtra("media", this.intent) as Media
        var cleanUrl = this.getAccount().url

        if (!cleanUrl.endsWith("/")) {
            cleanUrl += '/'
        }

        if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
            cleanUrl = "http://$cleanUrl"
        }

        cleanUrl += "explorer/html5/stream/token/" + this.media.token

        val videoView = findViewById<View>(R.id.video) as VideoView
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(cleanUrl), mapOf("X-Device-Token" to this.getAccount().token))

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
                            this.startVideo(videoView)
                        },
                        {
                            videoView.seekTo((position?.position ?: 0) * 1000)
                            this.startVideo(videoView)
                        },
                    ).show()
                } else {
                    this.startVideo(videoView)
                }
            }
        })

        videoView.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()

                return@setOnClickListener
            }

            videoView.start()
        }

        videoView.setOnCompletionListener {
            this.setResult(this.media.token.toString(), media.position ?: 0)
            this.finish()
        }
    }

    fun startVideo(videoView: VideoView) {
        videoView.start()

        this.runTask({
            this.savePosition(videoView, this.media.token.toString(), media.position ?: 0)
        })
    }

    private fun savePosition(videoView: VideoView, token: String, lastPosition: Int) {
        var newPosition = lastPosition

        if (videoView.currentPosition != lastPosition) {
            newPosition = videoView.currentPosition / 1000

            if (newPosition > 0) {
                try {
                    Html5Task.savePosition(this, token, newPosition)
                    this.media.position = newPosition

                    this.runOnUiThread {
                        this.setResult(token, newPosition)
                    }
                } catch (_: TaskException) {
                }
            }
        }

        Thread.sleep(1000)
        this.savePosition(videoView, token, newPosition)
    }

    private fun setResult(token: String, position: Int) {
        val result = Intent()
        result.putExtra("token", token)
        result.putExtra("position", position)
        this.setResult(RESULT_OK, result)
    }
}