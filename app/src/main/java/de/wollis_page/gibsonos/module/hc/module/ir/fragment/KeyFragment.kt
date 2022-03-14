package de.wollis_page.gibsonos.module.hc.module.ir.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.module.ir.dto.Key
import de.wollis_page.gibsonos.module.hc.module.task.IrTask

class KeyFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        TODO("Not yet implemented")
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

    override fun loadList() = this.load {
        this.listAdapter.items = IrTask.keys(this.getGibsonOsActivity()).toMutableList()
    }
}