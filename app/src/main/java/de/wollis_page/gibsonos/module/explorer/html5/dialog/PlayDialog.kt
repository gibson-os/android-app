package de.wollis_page.gibsonos.module.explorer.html5.dialog

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.FlattedDialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog

class PlayDialog(private val context: GibsonOsActivity) {
    fun build(
        duration: Int,
        position: Int,
        startAction: (FlattedDialogItem) -> Any,
        continueAction: (FlattedDialogItem) -> Any,
        ): AlertListDialog {
        val options = ArrayList<DialogItem>()

        if (position + 1 < duration || duration == 0) {
            val continueItem = DialogItem(this.context.getString(R.string.explorer_html5_continue))
            continueItem.icon = R.drawable.ic_play
            continueItem.onClick = continueAction
            options.add(continueItem)
        }

        val restartItem = DialogItem(this.context.getString(R.string.explorer_html5_play_again))
        restartItem.icon = R.drawable.ic_restart
        restartItem.onClick = startAction
        options.add(restartItem)

        return AlertListDialog(
            this.context,
            this.context.getString(
                R.string.explorer_html5_continue_message,
                this.transformSeconds(position),
                this.transformSeconds(duration)
            ),
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