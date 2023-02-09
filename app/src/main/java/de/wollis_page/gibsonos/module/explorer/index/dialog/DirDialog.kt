package de.wollis_page.gibsonos.module.explorer.index.dialog

import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.desktop.service.DialogItemService
import de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.Item

class DirDialog(private val context: IndexActivity) {
    fun build(item: Item): AlertListDialog {
        val options = ArrayList<DialogItem>()

        options.add(this.getDesktopItem(item))

        return AlertListDialog(this.context, item.name, options)
    }

    private fun getDesktopItem(item: Item): DialogItem {
        return DialogItemService.getDesktopItem(this.context, Shortcut(
            "explorer",
            "index",
            "index",
            item.name,
            "icon_dir",
            mapOf("dir" to this.context.loadedDir.dir + "/" + item.name)
        ))
    }
}