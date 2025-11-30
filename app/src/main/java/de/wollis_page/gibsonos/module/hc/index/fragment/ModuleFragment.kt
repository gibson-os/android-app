package de.wollis_page.gibsonos.module.hc.index.fragment

import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.desktop.service.DialogItemService
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.task.ModuleTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.AppIconService

class ModuleFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Module) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "hc",
                    item.type.lowercase().replace("\\W".toRegex(), ""),
                    "index",
                    mapOf<String, Parcelable>(
                        "module" to item,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun getListRessource() = R.layout.hc_module_list_item

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Module) {
            return
        }

        view.findViewById<ImageView>(R.id.icon).setImageResource(
            AppIconService.getIcon("hc", item.helper, "index")
                ?: R.drawable.ic_android
        )
        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.address).text = item.address.toString()
        view.findViewById<TextView>(R.id.type).text = item.type
        view.findViewById<TextView>(R.id.modified).text = item.modified
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        val masterId = this.fragmentsArguments["masterId"] as Long
        this.listAdapter.setListResponse(ModuleTask.index(
            this,
            masterId,
            start,
            limit
        ))
    }

    override fun onLongClick(item: ListItemInterface): Boolean {
        if (item !is Module) {
            return false
        }

        AlertListDialog(
            this.activity,
            item.name,
            arrayListOf(DialogItemService.getDesktopItem(this.activity, this.getShortcut(item)))
        ).show()

        return true
    }

    private fun getShortcut(item: Module): Shortcut {
        return Shortcut(
            "hc",
            "module",
            "view",
            item.name,
            (item.settings?.get("icon") ?: "icon_homecontrol") as String,
            mutableMapOf(
                "id" to item.id,
                "type" to item.type,
                "name" to item.name,
                "address" to item.address,
                "helper" to item.helper,
                "modified" to item.modified,
            )
        )
    }
}