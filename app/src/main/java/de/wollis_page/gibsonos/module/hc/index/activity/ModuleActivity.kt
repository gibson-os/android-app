package de.wollis_page.gibsonos.module.hc.index.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.helper.AppManager
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.task.ModuleTask

class ModuleActivity : ListActivity(), AppActivityInterface {
    private lateinit var master: Master

    override fun onCreate(savedInstanceState: Bundle?) {
        this.master = this.intent.getParcelableExtra("master")!!

        super.onCreate(savedInstanceState)

        this.setTitle(this.master.name)
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Module) {
            return
        }

        this.runTask({
            try {
                val activityClass = this.getModuleActivityClass(item.helper)
                val intent = Intent(this, activityClass)
                intent.putExtra(ACCOUNT_KEY, this.getAccount())
                intent.putExtra("module", item)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                this.startActivity(intent)
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Module) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.address).text = item.address.toString()
        view.findViewById<TextView>(R.id.type).text = item.type
        view.findViewById<TextView>(R.id.modified).text = item.modified
    }

    override fun loadList() = this.load {
        this.listAdapter.items = ModuleTask.index(this, this.master.id).toMutableList()
    }

    override fun getListRessource() = R.layout.hc_module_list_item

    override fun getAppIcon() = R.drawable.ic_stream

    private fun getModuleActivityClass(helper: String): Class<*> {
        val packageName = "de.wollis_page.gibsonos.module.hc.module.$helper.activity.IndexActivity"
        Log.i(Config.LOG_TAG, "Look for package: $packageName")

        return Class.forName(packageName)
    }
}