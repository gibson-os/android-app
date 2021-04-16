package de.wollis_page.gibsonos.module.core.desktop.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.AlertDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Item
import de.wollis_page.gibsonos.helper.Config
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

        val cronjobsSetting = DialogItem(this.getString(R.string.core_desktop_cronjobs))
        cronjobsSetting.icon = R.drawable.ic_calendar_alt

        val eventsSetting = DialogItem(this.getString(R.string.core_desktop_events))
        eventsSetting.icon = R.drawable.ic_stream

        val cogButton = this.findViewById<FloatingActionButton>(R.id.cogButton)
        cogButton.setOnClickListener {
            AlertDialog(
                this,
                this.getString(R.string.core_desktop_settings),
                arrayListOf(cronjobsSetting, eventsSetting)
            ).show()
        }

        this.loadDesktop()
    }

    private fun loadDesktop() = this.load {
        val desktop = DesktopTask.index(this)

        it.apps = desktop.apps
        this.listAdapter.items = desktop.desktop.toMutableList()
        this.loadNavigation()
    }

    override fun onCLick(item: ListItemInterface) {
        if (item !is Item) {
            return
        }

        val packageName =
            "de.wollis_page.gibsonos.module." +
            item.module + "." + item.task + ".activity.IndexActivity"

        try {
            Log.i(Config.LOG_TAG, "Look for package: $packageName")
            val activityClass = Class.forName(packageName)
            val intent = Intent(this, activityClass)
            intent.putExtra(ACCOUNT_KEY, this.getAccount())
            intent.putExtra(ITEM_KEY, item)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            this.startActivity(intent)
        } catch (exception: ClassNotFoundException) {
            Toast.makeText(this, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
        }
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Item) {
            return
        }

        (view.findViewById<View>(R.id.text) as TextView).text = item.text
        (view.findViewById<View>(R.id.module) as TextView).text = item.module + "_" + item.task
    }
}