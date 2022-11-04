package de.wollis_page.gibsonos.module.core.desktop.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.helper.AppManager
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.task.DesktopTask

class IndexActivity : ListActivity() {
    override fun getListRessource() = R.layout.desktop_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTitle()

        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            R.layout.base_button_cog,
            this.findViewById(android.R.id.content),
            false
        ))

        val cronjobsSetting = DialogItem(this.getString(R.string.core_cronjob_title))
        cronjobsSetting.icon = AppManager.getAppIcon("core", "cronjob")
        cronjobsSetting.onClick = {
            this.runTask({
                this.startActivity(
                    "core",
                    "cronjob",
                    "index",
                    0,
                    emptyMap()
                )
            })
        }

        val eventsSetting = DialogItem(this.getString(R.string.core_event_title))
        eventsSetting.icon = AppManager.getAppIcon("core", "event")
        eventsSetting.onClick = {
            this.runTask({
                this.startActivity(
                    "core",
                    "event",
                    "index",
                    0,
                    emptyMap()
                )
            })
        }

        val cogButton = this.findViewById<FloatingActionButton>(R.id.cogButton)
        cogButton.setOnClickListener {
            AlertListDialog(
                this,
                this.getString(R.string.core_desktop_settings),
                arrayListOf(cronjobsSetting, eventsSetting)
            ).show()
        }
    }

    override fun getId(): Any = this.getAccount().id

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = false

    override fun loadList(start: Long, limit: Long) = this.load {
        val desktop = DesktopTask.index(this)

        it.apps = desktop.apps
        this.listAdapter.items = desktop.desktop as ArrayList<ListItemInterface>
        this.loadNavigation()
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Shortcut) {
            return
        }

        this.runTask({
            try {
                this.startActivity(item, mapOf(SHORTCUT_KEY to item))
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Shortcut) {
            return
        }

        view.findViewById<TextView>(R.id.text).text = item.text
        view.findViewById<ImageView>(R.id.icon).setImageResource(
            AppManager.getAppIcon(item.module, item.task)
        )
    }
}