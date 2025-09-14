package de.wollis_page.gibsonos.module.hc.index.fragment

import android.os.Parcelable
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.desktop.service.DialogItemService
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.module.hc.task.MasterTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class MasterFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Master) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "hc",
                    "index",
                    "master",
                    mapOf<String, Parcelable>(
                        "master" to item,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Master) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.address).text = item.address
        view.findViewById<TextView>(R.id.modified).text = item.modified
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(MasterTask.index(this, start, limit))
    }

    override fun getListRessource() = R.layout.hc_master_list_item

    override fun onLongClick(item: ListItemInterface): Boolean {
        if (item !is Master) {
            return false
        }

        AlertListDialog(
            this.activity,
            item.name,
            arrayListOf(DialogItemService.getDesktopItem(this.activity, this.getShortcut(item)))
        ).show()

        return true
    }

    private fun getShortcut(item: Master): Shortcut {
        return Shortcut(
            "hc",
            "master",
            "view",
            item.name,
            "icon_homecontrol",
            mutableMapOf(
                "id" to item.id,
                "name" to item.name,
                "address" to item.address,
                "protocol" to item.protocol,
                "added" to item.added,
                "modified" to item.modified,
            )
        )
    }
}