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

class AlertListDialog(val context: GibsonOsActivity, private val title: String, val items: ArrayList<DialogItem>) {
    var layout: Int = R.layout.base_alert_dialog_item
//    this.items.toArray(arrayOfNulls<String>(0)
    fun show(): AlertDialog? {
        val adapter: ListAdapter = object : ArrayAdapter<DialogItem>(
            this.context,
            this.layout,
            R.id.name,
            this.items
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: View = super.getView(position, convertView, parent)

                val item = items[position]
                var icon = item.icon

                if (item.expanded && item.iconExpanded != null) {
                    icon = item.iconExpanded
                }

                if (icon != null) {
                    view.findViewById<ImageView>(R.id.icon).setImageResource(icon)
                }

                view.findViewById<TextView>(R.id.name).text = item.text

                return view
            }
        }

        return Builder(this.context)
            .setTitle(this.title)
            .setAdapter(adapter) { _, which ->
                val item = this.items[which]
                val onClick = item.onClick ?: return@setAdapter

                if (item.children != null && !item.fireOnClickOnExpand) {
                    return@setAdapter
                }

                onClick()
            }
            .show()
    }
}