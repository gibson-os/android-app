package de.wollis_page.gibsonos.module.hc.ir.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.desktop.service.DialogItemService
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.ir.dto.Remote
import de.wollis_page.gibsonos.module.hc.task.IrTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class RemoteFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Remote) {
            return
        }

        val module = this.fragmentsArguments["module"] as Module

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "hc",
                "ir",
                "remote",
                mapOf(
                    "module" to module,
                    "remoteId" to item.id,
                    GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                )
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Remote) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
    }

    override fun getListRessource(): Int = R.layout.hc_module_ir_remote_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        val module = this.fragmentsArguments["module"] as Module
        this.listAdapter.setListResponse(IrTask.remotes(
            this,
            module.id,
            start,
            limit
        ))
    }

    override fun onLongClick(item: ListItemInterface): Boolean {
        if (item !is Remote) {
            return false
        }

        AlertListDialog(
            this.activity,
            item.name,
            arrayListOf(DialogItemService.getDesktopItem(this.activity, this.getShortcut(item)))
        ).show()

        return true
    }

    private fun getShortcut(item: Remote): Shortcut {
        val module = this.fragmentsArguments["module"] as Module

        return Shortcut(
            "hc",
            "ir",
            "remote",
            item.name,
            "icon_remotecontrol",
            mutableMapOf(
                "moduleId" to module.id,
                "remoteId" to item.id,
            )
        )
    }
}