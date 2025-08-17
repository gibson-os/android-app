package de.wollis_page.gibsonos.helper

import android.app.AlertDialog
import android.app.AlertDialog.Builder
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogButton
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.FlattedDialogItem

class AlertListDialog(
    private val context: GibsonOsActivity,
    private val title: String,
    private val items: ArrayList<DialogItem>,
    private val positiveButton: DialogButton? = null,
    private val negativeButton: DialogButton? = null,
    private val neutralButton: DialogButton? = null,
) {
    private val flattedItems: ArrayList<FlattedDialogItem> = ArrayList()
    private var scrollTo: Int? = null
    private var dialog: AlertDialog? = null

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

    fun show(): AlertDialog {
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

                val container = view.findViewById<LinearLayout>(R.id.container)
                view.findViewById<TextView>(R.id.name).text = dialogItem.text

                var checkboxVisibility = View.GONE

                if (dialogItem.checkbox) {
                    checkboxVisibility = View.VISIBLE
                }

                val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
                checkbox.visibility = checkboxVisibility
                checkbox.isChecked = dialogItem.selected
                checkbox.setOnClickListener {
                    dialogItem.selected = checkbox.isChecked

                    if (dialogItem.onClickCheckbox != null) {
                        dialogItem.onClickCheckbox?.invoke(item, this)
                    }
                }

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val px = (16 *
                    (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
                ).toInt()
                layoutParams.setMargins(item.level * px, px, px, px)
                container.layoutParams = layoutParams
                container.visibility = View.VISIBLE

                if (item.parent?.dialogItem?.expanded == false) {
                    container.visibility = View.GONE
                }

                view.setOnClickListener {
                    val clickedFlattedItem = flattedItems[position]
                    val clickedDialogItem = clickedFlattedItem.dialogItem

                    if (clickedDialogItem.children != null && !clickedDialogItem.fireOnClickOnExpand) {
                        clickedDialogItem.expanded = !clickedDialogItem.expanded
                        notifyDataSetChanged()

                        return@setOnClickListener
                    }

                    clickedDialogItem.onClick?.invoke(clickedFlattedItem, this)

                    if (clickedFlattedItem.dialogItem.checkbox) {
                        checkbox.isChecked = !checkbox.isChecked
                        clickedFlattedItem.dialogItem.selected = checkbox.isChecked

                        return@setOnClickListener
                    }

                    this@AlertListDialog.dialog?.dismiss()
                }

                return view
            }
        }

        val scrollToItem = this.scrollTo
        val dialogBuilder = Builder(this.context)
            .setTitle(this.title)
            .setAdapter(adapter, null)

        if (this.positiveButton != null) {
            dialogBuilder.setPositiveButton(this.positiveButton.text) { _, _ ->
                this.positiveButton.onClick()
            }
        }

        if (this.negativeButton != null) {
            dialogBuilder.setNegativeButton(this.negativeButton.text) { _, _ ->
                this.negativeButton.onClick()
            }
        }

        if (this.neutralButton != null) {
            dialogBuilder.setNeutralButton(this.neutralButton.text) { _, _ ->
                this.neutralButton.onClick()
            }
        }

        this.dialog = dialogBuilder.create()

        if (scrollToItem != null) {
            this.dialog?.setOnShowListener {
                (it as AlertDialog).listView.smoothScrollToPosition(scrollToItem)
            }
        }

        this.dialog?.show()

        return this.dialog!!
    }
}