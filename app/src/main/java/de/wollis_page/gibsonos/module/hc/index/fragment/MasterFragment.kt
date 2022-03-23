package de.wollis_page.gibsonos.module.hc.index.fragment

import android.content.Intent
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.hc.index.activity.MasterActivity
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.module.hc.task.MasterTask

class MasterFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Master) {
            return
        }

        this.runTask({
            try {
                val intent = Intent(this.requireActivity(), MasterActivity::class.java)
                intent.putExtra(GibsonOsActivity.ACCOUNT_KEY, this.getAccount())
                intent.putExtra("master", item)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

                this.startActivity(intent)
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Master) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.address).text = item.address
        view.findViewById<TextView>(R.id.modified).text = item.modified
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(MasterTask.index(this.getGibsonOsActivity(), start, limit))
    }

    override fun getListRessource() = R.layout.hc_master_list_item
}