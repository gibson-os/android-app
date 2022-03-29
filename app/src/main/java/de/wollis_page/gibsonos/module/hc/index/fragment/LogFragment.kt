package de.wollis_page.gibsonos.module.hc.index.fragment

import android.text.Html
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.index.dto.Log
import de.wollis_page.gibsonos.module.hc.task.LogTask

class LogFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Log) {
            return
        }

        var text = item.rendered

        if (text == null) {
            text = item.text
        }

        if (text == null) {
            text = item.data
        }

        view.findViewById<TextView>(R.id.text).text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
        view.findViewById<TextView>(R.id.added).text = item.added
        view.findViewById<TextView>(R.id.master).text = item.masterName
        view.findViewById<TextView>(R.id.module).text = item.moduleName.toString()
        view.findViewById<TextView>(R.id.direction).text = item.direction
        view.findViewById<TextView>(R.id.type).text = item.type.toString()
        view.findViewById<TextView>(R.id.command).text = item.command.toString()
    }

    override fun getListRessource(): Int = R.layout.hc_log_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        val masterId = this.fragmentsArguments["masterId"] as Long?
        val moduleId = this.fragmentsArguments["moduleId"] as Long?
        this.listAdapter.setListResponse(LogTask.index(
            this.activity,
            start,
            limit,
            masterId,
            moduleId
        ))
    }
}