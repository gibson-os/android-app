package de.wollis_page.gibsonos.module.explorer.html5.dialog

import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.explorer.index.dto.DirList

class DirListDialog(private val context: GibsonOsActivity) {
    fun build(dirList: ListResponse<DirList>): AlertListDialog {
        val options = ArrayList<DialogItem>()

        return AlertListDialog(this.context, "Verzeichnis", options)
    }
}