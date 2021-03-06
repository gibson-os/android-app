package de.wollis_page.gibsonos.module.core.desktop.activity

import android.content.Intent
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
import de.wollis_page.gibsonos.helper.AlertDialog
import de.wollis_page.gibsonos.helper.AppManager
import de.wollis_page.gibsonos.module.core.desktop.dto.Item
import de.wollis_page.gibsonos.module.core.task.DesktopTask

class IndexActivity : ListActivity() {
    override fun getListRessource() = R.layout.desktop_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                val activityClass = AppManager.getActivityClass("core", "cronjob")
                val intent = Intent(this, activityClass)
                intent.putExtra(ACCOUNT_KEY, this.getAccount())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

                this.startActivity(intent)
            })
        }

        val eventsSetting = DialogItem(this.getString(R.string.core_event_title))
        eventsSetting.icon = AppManager.getAppIcon("core", "event")
        eventsSetting.onClick = {
            this.runTask({
                val activityClass = AppManager.getActivityClass("core", "event")
                val intent = Intent(this, activityClass)
                intent.putExtra(ACCOUNT_KEY, this.getAccount())
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

                this.startActivity(intent)
            })
        }

        val cogButton = this.findViewById<FloatingActionButton>(R.id.cogButton)
        cogButton.setOnClickListener {
            AlertDialog(
                this,
                this.getString(R.string.core_desktop_settings),
                arrayListOf(cronjobsSetting, eventsSetting)
            ).show()
        }
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        val desktop = DesktopTask.index(this)

        it.apps = desktop.apps
        this.listAdapter.items = desktop.desktop.toMutableList()
        this.loadNavigation()
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Item) {
            return
        }

        this.runTask({
            try {
                val activityClass = AppManager.getActivityClass(item.module, item.task)
                val intent = Intent(this, activityClass)
                intent.putExtra(ACCOUNT_KEY, this.getAccount())
                intent.putExtra(ITEM_KEY, item)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

                this.startActivity(intent)
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Item) {
            return
        }

        view.findViewById<TextView>(R.id.text).text = item.text
        view.findViewById<ImageView>(R.id.icon).setImageResource(
            AppManager.getAppIcon(item.module, item.task)
        )
    }
}