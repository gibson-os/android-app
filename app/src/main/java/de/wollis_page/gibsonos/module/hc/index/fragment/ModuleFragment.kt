package de.wollis_page.gibsonos.module.hc.index.fragment

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.task.ModuleTask

class ModuleFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Module) {
            return
        }

        this.runTask({
            try {
                val activityClass = this.getModuleActivityClass(item.helper)
                val intent = Intent(this.requireActivity(), activityClass)
                intent.putExtra(GibsonOsActivity.ACCOUNT_KEY, this.getAccount())
                intent.putExtra("module", item)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

                this.startActivity(intent)
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

        var imageRessource = R.drawable.ic_android

        try {
            val moduleActivity = this.getModuleActivityClass(item.helper).newInstance()

            if (moduleActivity is AppActivityInterface) {
                imageRessource = moduleActivity.getAppIcon()
            }
        } catch (exception: ClassNotFoundException) {
            Log.i(Config.LOG_TAG, "Class for hc module " + item.name + "doesnt exists")
        }

        view.findViewById<ImageView>(R.id.icon).setImageResource(imageRessource)
        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.address).text = item.address.toString()
        view.findViewById<TextView>(R.id.type).text = item.type
        view.findViewById<TextView>(R.id.modified).text = item.modified
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        val masterId = this.fragmentsArguments["masterId"] as Long
        this.listAdapter.setListResponse(ModuleTask.index(
            this.activity,
            masterId,
            start,
            limit
        ))
    }

    private fun getModuleActivityClass(helper: String): Class<*> {
        val packageName = "de.wollis_page.gibsonos.module.hc.$helper.activity.IndexActivity"
        Log.i(Config.LOG_TAG, "Look for package: $packageName")

        return Class.forName(packageName)
    }
}