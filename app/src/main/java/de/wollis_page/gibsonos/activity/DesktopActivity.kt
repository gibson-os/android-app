package de.wollis_page.gibsonos.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.dto.ListInterface
import de.wollis_page.gibsonos.dto.desktop.Item
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.task.DesktopTask
import java.util.*

class DesktopActivity : ListActivity() {
    override fun onCLick(item: ListInterface) {
        if (item !is Item) {
            return
        }

        val packageName =
            "de.wollis_page.gibsonos.module." +
            item.module + ".activity." +
            item.task.capitalize(Locale.ROOT) + "Activity"

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

    override fun bind(item: ListInterface, view: View) {
        if (item !is Item) {
            return
        }

        (view.findViewById<View>(R.id.text) as TextView).text = item.text
        (view.findViewById<View>(R.id.module) as TextView).text = item.module + "_" + item.task
    }

    override fun getListRessource() = R.layout.desktop_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_desktop)
        super.onCreate(savedInstanceState)

        this.loadDesktop()
    }

    private fun loadDesktop() = this.load {
        val desktop = DesktopTask.index(this, it.account)

        it.apps = desktop.apps
        this.adapter.items = desktop.desktop.toMutableList()
        this.loadNavigation()
    }
}