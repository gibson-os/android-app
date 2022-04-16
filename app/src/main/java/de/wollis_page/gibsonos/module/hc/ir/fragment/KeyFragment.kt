package de.wollis_page.gibsonos.module.hc.ir.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.ir.dto.Key
import de.wollis_page.gibsonos.module.hc.task.IrTask

class KeyFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) = this.load {
        if (item is Key) {
            val module = this.fragmentsArguments["module"] as Module
            IrTask.send(
                this.activity,
                module.id,
                item.protocol,
                item.address,
                item.command
            )
        }
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Key) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.protocol).text = item.protocolName
        view.findViewById<TextView>(R.id.address).text = item.address.toString()
        view.findViewById<TextView>(R.id.command).text = item.command.toString()
    }

    override fun getListRessource(): Int = R.layout.hc_module_ir_key_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(IrTask.keys(this.activity, start, limit))
    }
}