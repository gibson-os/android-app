package de.wollis_page.gibsonos.module.hc.module.io.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.module.io.dto.Port
import de.wollis_page.gibsonos.module.hc.module.task.IoTask

class IndexActivity : ListActivity(), AppActivityInterface {
    private lateinit var module: Module

    override fun onCreate(savedInstanceState: Bundle?) {
        this.module = this.intent.getParcelableExtra("module")!!

        super.onCreate(savedInstanceState)

        this.setTitle(this.module.name)
    }

    override fun onClick(item: ListItemInterface) = this.load {
        if (item is Port && item.direction == 1) {
            IoTask.toggle(this, this.module.id, item.number)
        }
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Port) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.status).text = item.valueNames[if (item.value) 1 else 0]
        view.findViewById<TextView>(R.id.direction).text =
            this.getString(if (item.direction == 0) R.string.hc_module_io_input else R.string.hc_module_io_output)
    }

    override fun loadList() = this.load {
        this.listAdapter.items = IoTask.ports(this, this.module.id).toMutableList()
    }

    override fun getListRessource() = R.layout.hc_module_io_list_item

    override fun getAppIcon() = R.drawable.ic_stream
}