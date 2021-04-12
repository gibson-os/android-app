package de.wollis_page.gibsonos.module.explorer.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.toHumanReadableByte
import de.wollis_page.gibsonos.module.explorer.dto.Item
import de.wollis_page.gibsonos.module.explorer.task.DirTask

class IndexActivity: ListActivity() {
    override fun getListRessource() = R.layout.explorer_index_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.loadList((this.getItem().params?.get("dir") ?: "").toString())
    }

    private fun loadList(directory: String = "") = this.load {
        Log.i(Config.LOG_TAG, "Read dir $directory")
        val dir = DirTask.read(this, it.account, directory)

        this.adapter.items = dir.data.toMutableList()
    }

    override fun onCLick(item: ListItemInterface) {
        if (item !is Item) {
            return
        }

        if (item.type == "dir") {
            this.loadList(item.path + "/" + item.name)

            return
        }

        Toast.makeText(this, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Item) {
            return
        }

        if (item.type != "dir") {
            (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(R.drawable.ic_file)
        } else {
            (view.findViewById<View>(R.id.icon) as ImageView).setImageResource(R.drawable.ic_folder)
        }

        if (item.html5VideoToken != null) {
            (view.findViewById<View>(R.id.html5) as ImageView).visibility = View.VISIBLE
        } else {
            (view.findViewById<View>(R.id.html5) as ImageView).visibility = View.INVISIBLE
        }

        (view.findViewById<View>(R.id.name) as TextView).text = item.name
        (view.findViewById<View>(R.id.size) as TextView).text = item.size.toHumanReadableByte()
    }
}