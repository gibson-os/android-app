package de.wollis_page.gibsonos.dialog

import android.util.Log
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.response.Filter
import de.wollis_page.gibsonos.dto.response.filter.Option
import de.wollis_page.gibsonos.helper.AlertListDialog
import de.wollis_page.gibsonos.helper.Config

class FilterDialog(private val context: GibsonOsActivity) {
    fun build(filters: MutableMap<String, Filter>): AlertListDialog {
        val options = ArrayList<DialogItem>()

        filters.forEach {
            val filterItem = this.getFilterItem(it.key, it.value)

            options.add(filterItem)
        }

        return AlertListDialog(this.context, this.context.getString(R.string.filter), options)
    }

    private fun getFilterItem(key: String, filter: Filter): DialogItem {
        val dialogItem = DialogItem(filter.name)
        dialogItem.icon = R.drawable.ic_filter_menu
        dialogItem.expanded = false
        dialogItem.fireOnClickOnExpand = false
        dialogItem.children = this.getOptionItem(filter.options)

        return dialogItem
    }

    private fun getOptionItem(options: MutableList<Option>): MutableList<DialogItem> {
        val children = mutableListOf<DialogItem>()

        options.forEach {
            val dialogItem = DialogItem(it.name)
            dialogItem.icon = R.drawable.ic_filter_menu
            dialogItem.onClick = {
                Log.d(Config.LOG_TAG, "Option clicked: ${it.dialogItem.text}")
            }

            children.add(dialogItem)
        }

        return children
    }
}