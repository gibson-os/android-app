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
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.explorer.html5.dialog.PlayDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.Media
import de.wollis_page.gibsonos.module.explorer.task.Html5Task

class PlayerActivity: GibsonOsActivity() {
    private lateinit var media: Media

    override fun getContentView(): Int = R.layout.explorer_html5_player

    override fun getId(): Any = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.removeHeder()

        this.media = this.intent.getParcelableExtra("media")!!
        var cleanUrl = this.intent.getParcelableExtra<Account>("account")!!.url

        if (!cleanUrl.endsWith("/")) {
            cleanUrl += '/'
        }

        if (!cleanUrl.startsWith("http://") && !cleanUrl.startsWith("https://")) {
            cleanUrl = "http://$cleanUrl"
        }

        cleanUrl += "explorer/html5/video/token/" + this.media.token

        val videoView = findViewById<View>(R.id.video) as VideoView
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(Uri.parse(cleanUrl))
        val position = this.media.position ?: 0

        if (position > 0) {
            PlayDialog(this).build(videoView, media).show()
        } else {
            this.startVideo(videoView)
        }

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