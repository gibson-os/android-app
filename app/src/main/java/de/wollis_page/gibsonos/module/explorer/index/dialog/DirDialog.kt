package de.wollis_page.gibsonos.module.explorer.index.dialog

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.Item

class DirDialog(private val context: IndexActivity) {
    fun build(item: Item): AlertListDialog {
        val options = ArrayList<DialogItem>()

        options.add(this.getDesktopItem(item))

        return AlertListDialog(this.context, item.name, options)
    }

    private fun getDesktopItem(item: Item): DialogItem {
        val dialogItem = DialogItem(this.context.getString(R.string.core_desktop_add))

        dialogItem.icon = R.drawable.ic_plus
        dialogItem.onClick = {

        }

        return dialogItem
    }
}