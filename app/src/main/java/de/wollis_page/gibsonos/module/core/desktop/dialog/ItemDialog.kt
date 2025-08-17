package de.wollis_page.gibsonos.module.core.desktop.dialog

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.activity.IndexActivity
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.AppIconService

class ItemDialog(private val context: IndexActivity) {
    fun build(): AlertListDialog {
        val cronjobsSetting = DialogItem(this.context.getString(R.string.core_cronjob_title))
        cronjobsSetting.icon = AppIconService.getIcon("core", "cronjob", "index")
            ?: R.drawable.ic_android
        cronjobsSetting.onClick = { flattedItem, adapter ->
            this.context.runTask({
                ActivityLauncherService.startActivity(
                    this.context,
                    "core",
                    "cronjob",
                    "index",
                    emptyMap()
                )
            })
        }

        val eventsSetting = DialogItem(this.context.getString(R.string.core_event_title))
        eventsSetting.icon = AppIconService.getIcon("core", "event", "index")
            ?: R.drawable.ic_android
        eventsSetting.onClick = { flattedItem, adapter ->
            this.context.runTask({
                ActivityLauncherService.startActivity(
                    this.context,
                    "core",
                    "event",
                    "index",
                    emptyMap()
                )
            })
        }

        val messageSetting = DialogItem(this.context.getString(R.string.core_message_title))
        messageSetting.icon = AppIconService.getIcon("core", "message", "index")
            ?: R.drawable.ic_android
        messageSetting.onClick = { flattedItem, adapter ->
            this.context.runTask({
                ActivityLauncherService.startActivity(
                    this.context,
                    "core",
                    "message",
                    "index",
                    emptyMap()
                )
            })
        }

        return AlertListDialog(
            this.context,
            this.context.getString(R.string.core_desktop_settings),
            arrayListOf(cronjobsSetting, eventsSetting, messageSetting)
        )
    }
}