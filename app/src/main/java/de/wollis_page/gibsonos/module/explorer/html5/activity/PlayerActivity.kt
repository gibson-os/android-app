package de.wollis_page.gibsonos.module.explorer.html5.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.explorer.index.dto.Media

class PlayerActivity: AppCompatActivity() {
    private lateinit var media: Media

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setContentView(R.layout.explorer_html5_player)
        super.onCreate(savedInstanceState)

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
        videoView.start()
        //this.savePosition(videoPlayer, media.position ?: 0)

        videoView.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()

                return@setOnClickListener
            }

            videoView.start()
        }

        videoView.setOnCompletionListener {
            this.finish()
        }
    }

    private fun savePosition(videoView: VideoView, lastPosition: Int) {
        var newPosition = lastPosition

        if (videoView.currentPosition > lastPosition) {
            Log.d(Config.LOG_TAG, "save position: " + videoView.currentPosition.toString())
            newPosition = videoView.currentPosition
        }

        Thread.sleep(1000)
        savePosition(videoView, newPosition)
    }
}