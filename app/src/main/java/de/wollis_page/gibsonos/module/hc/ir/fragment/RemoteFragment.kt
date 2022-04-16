package de.wollis_page.gibsonos.module.hc.ir.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.index.dto.Module
import de.wollis_page.gibsonos.module.hc.ir.dto.Remote
import de.wollis_page.gibsonos.module.hc.task.IrTask

class RemoteFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Remote) {
            return
        }

        val module = this.fragmentsArguments["module"] as Module

        this.runTask({
            this.activity.startActivity(
                "hc",
                "ir",
                "remote",
                module.id.toString() + '_' + item.id.toString(),
                mapOf(
                    "module" to module,
                    "remoteId" to item.id,
                )
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Remote) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
    }

    override fun getListRessource(): Int = R.layout.hc_module_ir_remote_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        val module = this.fragmentsArguments["module"] as Module
        this.listAdapter.setListResponse(
            IrTask.remotes(
            this.activity,
            module.id,
            start,
            limit
        ))
    }
}