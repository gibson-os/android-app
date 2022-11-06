package de.wollis_page.gibsonos.helper

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.*
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.FlattedDialogItem

class AlertListDialog(
    private val context: GibsonOsActivity,
    private val title: String,
    private val items: ArrayList<DialogItem>
) {
    private val flattedItems: ArrayList<FlattedDialogItem> = ArrayList()
    private var scrollTo: Int? = null

    init {
        this.flatItems(this.items)
    }

    private fun flatItems(items: List<DialogItem>, level: Int = 1, parent: FlattedDialogItem? = null)  {
        items.forEach {
            val flattedDialogItem = FlattedDialogItem(it, level, parent)
            this.flattedItems.add(flattedDialogItem)
            val children = it.children

            if (it.scrollTo) {
                this.scrollTo = this.flattedItems.count()
            }

            if (children != null) {
                this.flatItems(children, level + 1, flattedDialogItem)
            }
        }
    }

    var layout: Int = R.layout.base_alert_dialog_item

    fun show(): AlertDialog? {
        val adapter: ListAdapter = object : ArrayAdapter<FlattedDialogItem>(
            this.context,
            this.layout,
            R.id.name,
            this.flattedItems
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view: View = super.getView(position, convertView, parent)

                val item = flattedItems[position]
                val dialogItem = item.dialogItem
                var icon = dialogItem.icon

                if (dialogItem.expanded && dialogItem.iconExpanded != null) {
                    icon = dialogItem.iconExpanded
                }

                if (icon != null) {
                    view.findViewById<ImageView>(R.id.icon).setImageResource(icon)
                }

                view.findViewById<TextView>(R.id.name).text = dialogItem.text
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val px = (16 *
                    (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                ).toInt()
                layoutParams.setMargins(item.level * px, px, px, px)
                view.findViewById<LinearLayout>(R.id.container).layoutParams = layoutParams

                return view
            }
        }

        val scrollToItem = this.scrollTo
        val dialog = Builder(this.context)
            .setTitle(this.title)
            .setAdapter(adapter) { _, which ->
                val flattedItem = this.flattedItems[which]
                val item = flattedItem.dialogItem
                val onClick = item.onClick ?: return@setAdapter

                if (item.children != null && !item.fireOnClickOnExpand) {
                    return@setAdapter
                }

                onClick(flattedItem)
            }
            .create()

        if (scrollToItem != null) {
            dialog.setOnShowListener {
                dialog.listView.smoothScrollToPosition(scrollToItem)
            }
        }

        dialog.show()

        return dialog
    }
}