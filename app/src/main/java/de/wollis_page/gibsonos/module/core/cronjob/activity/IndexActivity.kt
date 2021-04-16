package de.wollis_page.gibsonos.module.core.cronjob.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.core.cronjob.dto.Cronjob
import de.wollis_page.gibsonos.module.core.task.CronjobTask

class IndexActivity: ListActivity(), AppActivityInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.core_cronjob_title)
        this.loadCronjobs()
    }

    private fun loadCronjobs() = this.load {
        this.listAdapter.items = CronjobTask.index(this).toMutableList()
    }

    override fun onClick(item: ListItemInterface) {
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Cronjob) {
            return
        }

        view.findViewById<TextView>(R.id.command).text = item.command
        view.findViewById<TextView>(R.id.lastRun).text = this.getString(
            R.string.core_cronjob_last_run,
            item.lastRun ?: this.getString(R.string.core_cronjob_last_run_none)
        )
    }

    override fun getListRessource() = R.layout.core_cronjob_index_list_item

    override fun getAppIcon() = R.drawable.ic_calendar_alt
}