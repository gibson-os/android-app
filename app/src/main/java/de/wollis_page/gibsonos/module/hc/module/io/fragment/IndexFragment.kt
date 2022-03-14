package de.wollis_page.gibsonos.module.hc.module.io.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.module.io.dto.Port
import de.wollis_page.gibsonos.module.hc.module.task.IoTask

class IndexFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) = this.load {
        if (item is Port && item.direction == 1) {
            val moduleId = this.fragmentsArguments["moduleId"] as Long
            IoTask.toggle(this.getGibsonOsActivity(), moduleId, item.number)
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
        val moduleId = this.fragmentsArguments["moduleId"] as Long
        this.listAdapter.items = IoTask.ports(this.getGibsonOsActivity(), moduleId).toMutableList()
    }

    override fun getListRessource() = R.layout.hc_module_io_list_item
}