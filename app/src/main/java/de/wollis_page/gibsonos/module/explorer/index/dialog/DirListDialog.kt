package de.wollis_page.gibsonos.module.explorer.index.dialog

import android.util.Log
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.explorer.index.activity.IndexActivity
import de.wollis_page.gibsonos.module.explorer.index.dto.DirList

class DirListDialog(private val context: IndexActivity) {
    fun build(dirList: ListResponse<DirList>): AlertListDialog {
        Log.d(Config.LOG_TAG, dirList.data.toString())

        return AlertListDialog(this.context, "Verzeichnis", this.getDialogItems(dirList.data))
    }

    private fun getDialogItems(data: MutableList<DirList>): ArrayList<DialogItem> {
        val dialogItems = ArrayList<DialogItem>()

        data.forEach {
            val dirList = it
            val dialogItem = DialogItem(dirList.text, dirList.id)
            dialogItem.icon = R.drawable.ic_folder
            dialogItem.iconExpanded = R.drawable.ic_folder_open
            dialogItem.expanded = dirList.expanded
            dialogItem.scrollTo = dirList.expanded
            dialogItem.fireOnClickOnExpand = true
            dialogItem.onClick = {
                this.context.loadList(dirList.id)
            }
            val children = dirList.data

            if (children != null) {
                dialogItem.children = this.getDialogItems(children)
            }

            dialogItems.add(dialogItem)
        }

        return dialogItems
    }
}