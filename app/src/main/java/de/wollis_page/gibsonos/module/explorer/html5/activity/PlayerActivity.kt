package de.wollis_page.gibsonos.module.explorer.html5.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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

        Log.d(Config.LOG_TAG, "Before player")
        val videoPlayer = findViewById<View>(R.id.video) as VideoView
        Log.d(Config.LOG_TAG, "Before set uri")
        videoPlayer.setVideoURI(Uri.parse(cleanUrl))
        Log.d(Config.LOG_TAG, "Before start")
        videoPlayer.start()
        Log.d(Config.LOG_TAG, "Player Finish")
    }
}