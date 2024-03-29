package de.wollis_page.gibsonos.module.obscura.index.activity

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.desktop.service.DialogItemService
import de.wollis_page.gibsonos.module.obscura.scanner.dto.Scanner
import de.wollis_page.gibsonos.module.obscura.task.IndexTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class IndexActivity: ListActivity() {
    override fun getId(): Long = 0

    override fun onClick(item: ListItemInterface) {
        if (item !is Scanner) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "obscura",
                "scanner",
                "form",
                mapOf(
                    "deviceName" to item.deviceName,
                    "vendor" to item.vendor,
                    "model" to item.model,
                    SHORTCUT_KEY to this.getShortcut(item)
                )
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Scanner) {
            return
        }

        view.findViewById<TextView>(R.id.deviceName).text = item.deviceName
        view.findViewById<TextView>(R.id.vendor).text = item.vendor
        view.findViewById<TextView>(R.id.model).text = item.model
    }

    override fun getListRessource(): Int = R.layout.obscura_scanner_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(IndexTask.getScanner(this, start, limit))
    }

    override fun onLongClick(item: ListItemInterface): Boolean {
        if (item !is Scanner) {
            return false
        }

        AlertListDialog(
            this.activity,
            item.deviceName,
            arrayListOf(DialogItemService.getDesktopItem(this.activity, this.getShortcut(item)))
        ).show()

        return true
    }

    private fun getShortcut(item: Scanner): Shortcut {
        return Shortcut(
            "obscura",
            "scanner",
            "form",
            item.deviceName,
            "icon_scan",
            mutableMapOf(
                "deviceName" to item.deviceName,
                "model" to item.model,
                "vendor" to item.vendor,
            )
        )
    }
}