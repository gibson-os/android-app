package de.wollis_page.gibsonos.module.explorer.html5.dialog

import android.widget.VideoView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.html5.activity.PlayerActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.Media

class PlayDialog(private val context: PlayerActivity) {
    fun build(videoView: VideoView, media: Media): AlertListDialog {
        val options = ArrayList<DialogItem>()
        val position = media.position ?: 0

        val continueItem = DialogItem("Fortsetzen")
        continueItem.icon = R.drawable.ic_play
        continueItem.onClick = {
            videoView.seekTo(position * 1000)
            this.context.startVideo(videoView)
        }
        options.add(continueItem)

        val restartItem = DialogItem("Abspielen")
        restartItem.icon = R.drawable.ic_restart
        restartItem.onClick = {
            this.context.startVideo(videoView)
        }
        options.add(restartItem)

        return AlertListDialog(
            this.context,
            "Bereits " + this.transformSeconds(position) + " von " + this.transformSeconds(media.duration) + " gesehen.",
            options
        )
    }

    private fun transformSeconds(transformSeconds: Int): String {
        var minutes = transformSeconds / 60
        val hours = minutes / 60
        val seconds = transformSeconds - minutes * 60
        minutes -= hours * 60

        return hours.toString().padStart(2, '0') + ":" +
                minutes.toString().padStart(2, '0') + ":" +
                seconds.toString().padStart(2, '0')
    }
}