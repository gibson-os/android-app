package de.wollis_page.gibsonos.helper

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.InflattedDialogItem

class AlertListDialog() {
    lateinit var context: GibsonOsActivity
    private lateinit var title: String
    private val items: ArrayList<InflattedDialogItem> = ArrayList()

    constructor(context: GibsonOsActivity, title: String, items: ArrayList<DialogItem>) : this() {
        this.context = context
        this.title = title
        this.inflateItems(items)
    }

    private fun inflateItems(items: List<DialogItem>, level: Int = 0)  {
        items.forEach {
            this.items.add(InflattedDialogItem(it, level))
            val children = it.children

            if (children != null) {
                this.inflateItems(children, level + 1)
            }
        }
    }

    // @todo konstruktor der items inflated. Dabei die Ebene als zahl dran schreiben (und den parent)
    var layout: Int = R.layout.base_alert_dialog_item
//    this.items.toArray(arrayOfNulls<String>(0)
    fun show(): AlertDialog? {
        val adapter: ListAdapter = object : ArrayAdapter<InflattedDialogItem>(
            this.context,
            this.layout,
            R.id.name,
            this.items
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: View = super.getView(position, convertView, parent)

                val item = items[position]
                val dialogItem = item.dialogItem
                var icon = dialogItem.icon

                if (dialogItem.expanded && dialogItem.iconExpanded != null) {
                    icon = dialogItem.iconExpanded
                }

                if (icon != null) {
                    view.findViewById<ImageView>(R.id.icon).setImageResource(icon)
                }

                view.findViewById<TextView>(R.id.name).text = dialogItem.text

                return view
            }
        }

        return Builder(this.context)
            .setTitle(this.title)
            .setAdapter(adapter) { _, which ->
                val item = this.items[which].dialogItem
                val onClick = item.onClick ?: return@setAdapter

                if (item.children != null && !item.fireOnClickOnExpand) {
                    return@setAdapter
                }

                onClick()
            }
            .show()
    }
}