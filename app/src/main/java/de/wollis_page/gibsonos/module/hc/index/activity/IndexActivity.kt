package de.wollis_page.gibsonos.module.hc.index.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.module.hc.index.dto.Master
import de.wollis_page.gibsonos.module.hc.task.MasterTask

class IndexActivity : ListActivity(), AppActivityInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.hc_title)
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Master) {
            return
        }

        this.runTask({
            try {
                val intent = Intent(this, MasterActivity::class.java)
                intent.putExtra(ACCOUNT_KEY, this.getAccount())
                intent.putExtra("master", item)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

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

    override fun loadList() = this.load {
        this.listAdapter.items = MasterTask.index(this).toMutableList()
    }

    override fun getListRessource() = R.layout.hc_master_list_item

    override fun getAppIcon() = R.drawable.ic_stream
}