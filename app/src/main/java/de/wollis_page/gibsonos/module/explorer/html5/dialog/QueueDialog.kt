package de.wollis_page.gibsonos.module.explorer.html5.dialog

import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.html5.dto.Position
import de.wollis_page.gibsonos.module.explorer.service.ChromecastService

class QueueDialog(private val context: ChromecastService) {
    fun build(
        mediaInfo: MediaInfo,
        duration: Long,
        position: Position?,
    ): AlertListDialog {
        val positionValue = position?.position ?: 0
        val options = ArrayList<DialogItem>()
        val addItem = DialogItem(this.context.context.getString(R.string.explorer_html5_add_to_playlist))
        addItem.icon = R.drawable.ic_plus
        addItem.onClick = {
            this.context.addMedia(mediaInfo)
        }
        options.add(addItem)

        var startItemText = this.context.context.getString(R.string.explorer_html5_play)
        var startItemIcon = R.drawable.ic_play

        if (positionValue > 0 && (positionValue + 1 < duration || duration.toInt() == 0)) {
            val continueItem = DialogItem(this.context.context.getString(R.string.explorer_html5_continue))
            continueItem.icon = R.drawable.ic_play
            continueItem.onClick = {
                this.context.playMedia(mediaInfo, positionValue.toLong() * 1000)
            }
            options.add(continueItem)
            startItemText = this.context.context.getString(R.string.explorer_html5_play_again)
            startItemIcon = R.drawable.ic_restart
        }

        val startItem = DialogItem(startItemText)
        startItem.icon = startItemIcon
        startItem.onClick = {
            this.context.playMedia(mediaInfo)
        }
        options.add(startItem)

        return AlertListDialog(
            this.context.context,
            mediaInfo.metadata?.getString(MediaMetadata.KEY_TITLE).toString(),
            options
        )
    }
}