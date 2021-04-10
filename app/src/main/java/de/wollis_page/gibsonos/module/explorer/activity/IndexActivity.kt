package de.wollis_page.gibsonos.module.explorer.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.dto.ListInterface
import de.wollis_page.gibsonos.module.explorer.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.DirTask

class IndexActivity: ListActivity() {
    override fun onCLick(item: ListInterface) {
        if (item !is Item) {
            return
        }

        if (item.type == "dir") {
            this.loadList(item.name)

            return
        }

        Toast.makeText(this, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
    }

    override fun bind(item: ListInterface, view: View) {
        if (item !is Item) {
            return
        }

        (view.findViewById<View>(R.id.name) as TextView).text = item.name
        (view.findViewById<View>(R.id.path) as TextView).text = item.path
    }

    override fun getListRessource() = R.layout.explorer_index_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.explorer_index_activity)
        super.onCreate(savedInstanceState)

        this.loadList()
    }

    private fun loadList(directory: String = "") = this.load {
        val dir = DirTask.read(this, it.account, directory)

        this.adapter.items = dir.data.toMutableList()
    }
}