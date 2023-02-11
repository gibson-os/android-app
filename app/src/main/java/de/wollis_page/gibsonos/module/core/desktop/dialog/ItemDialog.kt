package de.wollis_page.gibsonos.module.core.desktop.dialog

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.activity.IndexActivity
import de.wollis_page.gibsonos.service.AppIconService

class ItemDialog(private val context: IndexActivity) {
    fun build(): AlertListDialog {
        val cronjobsSetting = DialogItem(this.context.getString(R.string.core_cronjob_title))
        cronjobsSetting.icon = AppIconService.getIcon("core", "cronjob", "index")
            ?: R.drawable.ic_android
        cronjobsSetting.onClick = {
            this.context.runTask({
                this.context.startActivity(
                    "core",
                    "cronjob",
                    "index",
                    0,
                    emptyMap()
                )
            })
        }

        val eventsSetting = DialogItem(this.context.getString(R.string.core_event_title))
        eventsSetting.icon = AppIconService.getIcon("core", "event", "index")
            ?: R.drawable.ic_android
        eventsSetting.onClick = {
            this.context.runTask({
                this.context.startActivity(
                    "core",
                    "event",
                    "index",
                    0,
                    emptyMap()
                )
            })
        }

        val messageSetting = DialogItem(this.context.getString(R.string.core_message_title))
        messageSetting.icon = AppIconService.getIcon("core", "message", "index")
            ?: R.drawable.ic_android
        messageSetting.onClick = {
            this.context.runTask({
                this.context.startActivity(
                    "core",
                    "message",
                    "index",
                    0,
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