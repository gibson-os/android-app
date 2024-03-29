package de.wollis_page.gibsonos.module.explorer.index.dialog

import android.os.Parcelable
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.html5.dialog.ConvertDialog
import de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.Html5Status
import de.wollis_page.gibsonos.module.explorer.index.dto.Item
import de.wollis_page.gibsonos.module.explorer.index.dto.Media
import de.wollis_page.gibsonos.module.explorer.service.Html5Service
import de.wollis_page.gibsonos.service.ActivityLauncherService

class ItemDialog(private val context: IndexActivity) {
    fun build(item: Item): AlertListDialog {
        val options = ArrayList<DialogItem>()

        if (item.category == 2 || item.category == 4) {
            if (item.html5VideoStatus == null) {
                options.add(this.getConvertItem(item))
            } else if (item.html5VideoStatus == Html5Status.GENERATED) {
                options.add(this.getPlayItem(item))
                options.add(this.getStreamItem(item))
            }
        }

        return AlertListDialog(this.context, item.name, options)
    }

    private fun getConvertItem(item: Item): DialogItem {
        val dialogItem = DialogItem(this.context.getString(R.string.explorer_html5_convert))

        dialogItem.icon = R.drawable.ic_html5
        dialogItem.onClick = {
            Html5Service(ConvertDialog(this.context)).convert(
                this.context,
                this.context.loadedDir.dir,
                item
            ) {
                item.html5VideoToken = it[this.context.loadedDir.dir + "/" + item.name]
                item.html5VideoStatus = Html5Status.WAIT

                this.context.runOnUiThread {
                    this.context.listAdapter.notifyItemChanged(this.context.getItemIndex(item))
                }
            }
        }

        return dialogItem
    }

    private fun getPlayItem(item: Item): DialogItem {
        val dialogItem = DialogItem(this.context.getString(R.string.explorer_html5_play))

        dialogItem.icon = R.drawable.ic_play
        dialogItem.onClick = {
            ActivityLauncherService.startActivity(
                this.context,
                "explorer",
                "html5",
                "player",
                mapOf<String, Parcelable>("media" to Media(
                    item.name,
                    item.html5VideoToken,
                    (item.metaInfos?.get("duration") ?: 0).toString().toFloat().toInt(),
                    item.position,
                )),
                this.context.playerLauncher
            )
        }

        return dialogItem
    }

    private fun getStreamItem(item: Item): DialogItem {
        val dialogItem = DialogItem(this.context.getString(R.string.explorer_html5_stream))
        val castSession = this.context.chromecastService.castSession

        dialogItem.icon = R.drawable.ic_chromecast
        dialogItem.onClick = {
            if (castSession === null) {
                this.context.chromecastService.showMediaRouterDialog()
                // Show media route chooser dialog
            } else {
                this.context.chromecastService.loadMedia(item)
            }
        }

        return dialogItem
    }
}