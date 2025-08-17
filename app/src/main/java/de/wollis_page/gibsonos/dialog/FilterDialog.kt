package de.wollis_page.gibsonos.dialog

import android.util.Log
import android.widget.ArrayAdapter
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogButton
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.FlattedDialogItem
import de.wollis_page.gibsonos.dto.response.Filter
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.helper.Config

class FilterDialog(private val context: GibsonOsActivity) {
    fun build(filters: MutableMap<String, Filter>): AlertListDialog {
        val options = ArrayList<DialogItem>()

        filters.forEach {
            val filterItem = this.getFilterItem(it.key, it.value)

            options.add(filterItem)
        }

        return AlertListDialog(
            this.context,
            this.context.getString(R.string.filter),
            options,
            DialogButton("Anwenden") {
                options.forEach { filter ->
                    filter.children?.forEach {
                        Log.d(Config.LOG_TAG, it.text)
                        Log.d(Config.LOG_TAG, it.selected.toString())
                    }
                }
            },
            DialogButton("Abbrechen") {
            }
        )
    }

    private fun getFilterItem(key: String, filter: Filter): DialogItem {
        val dialogItem = DialogItem(filter.name)
        dialogItem.icon = R.drawable.ic_filter_menu
        dialogItem.expanded = false
        dialogItem.fireOnClickOnExpand = false
        dialogItem.children = this.getOptionItem(filter)
        dialogItem.checkbox = true
        dialogItem.selected = true
        val onClick: ((flattedItem: FlattedDialogItem, adapter: ArrayAdapter<FlattedDialogItem>) -> Any?) = { flattedItem, adapter ->
            dialogItem.children?.forEach {
                it.selected = dialogItem.selected
            }

            adapter.notifyDataSetChanged()
        }
        dialogItem.onClick = onClick
        dialogItem.onClickCheckbox = onClick

        return dialogItem
    }

    private fun getOptionItem(filter: Filter): MutableList<DialogItem> {
        val children = mutableListOf<DialogItem>()

        filter.options.forEach {
            val dialogItem = DialogItem(it.name)
            dialogItem.icon = R.drawable.ic_filter_menu
            dialogItem.checkbox = true
            dialogItem.selected = true

            children.add(dialogItem)
        }

        return children
    }
}