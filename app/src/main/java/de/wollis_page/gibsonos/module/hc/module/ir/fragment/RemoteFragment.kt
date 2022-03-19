package de.wollis_page.gibsonos.module.hc.module.ir.fragment

import android.content.Intent
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.module.ir.activity.RemoteActivity
import de.wollis_page.gibsonos.module.hc.module.ir.dto.Remote
import de.wollis_page.gibsonos.module.hc.module.task.IrTask

class RemoteFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Remote) {
            return
        }

        this.runTask({
            val intent = Intent(this.requireActivity(), RemoteActivity::class.java)
            intent.putExtra(GibsonOsActivity.ACCOUNT_KEY, this.getAccount())
            intent.putExtra("moduleId", this.fragmentsArguments["moduleId"] as Long)
            intent.putExtra("remoteId", item.id)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            this.startActivity(intent)
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
        val moduleId = this.fragmentsArguments["moduleId"] as Long
        this.listAdapter.setListResponse(IrTask.remotes(
            this.getGibsonOsActivity(),
            moduleId,
            start,
            limit
        ))
    }
}