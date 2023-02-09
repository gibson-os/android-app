package de.wollis_page.gibsonos.module.core.event.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.desktop.service.DialogItemService
import de.wollis_page.gibsonos.module.core.event.dto.Event
import de.wollis_page.gibsonos.module.core.task.EventTask

class IndexActivity: ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.core_event_title)
    }

    override fun getId(): Any = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = false

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(EventTask.index(this, start, limit))
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Event) {
            return
        }

        val startEvent = DialogItem(this.getString(R.string.core_event_execute))
        startEvent.icon = R.drawable.ic_play
        startEvent.onClick = {
            this.runTask({
                EventTask.run(this, item.id)
            })
        }

        val deleteEvent = DialogItem(this.getString(R.string.core_event_remove))
        deleteEvent.icon = R.drawable.ic_minus

        AlertListDialog(
            this,
            item.name,
            arrayListOf(startEvent, deleteEvent)
        ).show()
    }

    override fun onLongClick(item: ListItemInterface): Boolean {
        if (item !is Event) {
            return false
        }

        AlertListDialog(
            this,
            item.name,
            arrayListOf(DialogItemService.getDesktopItem(this, Shortcut(
                "core",
                "event",
                "run",
                item.name,
                "icon_exe",
                mapOf("eventId" to item.id)
            )))
        ).show()

        return true
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Event) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.lastRun).text = this.getString(
            R.string.core_event_last_run,
            item.lastRun ?: this.getString(R.string.core_event_last_run_none)
        )
    }

    override fun getListRessource() = R.layout.core_event_index_list_item
}