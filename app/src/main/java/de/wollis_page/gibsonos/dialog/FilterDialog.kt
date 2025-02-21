package de.wollis_page.gibsonos.dialog

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dialog.filter.OptionDialog
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.response.Filter
import de.wollis_page.gibsonos.helper.AlertListDialog

class FilterDialog(private val context: GibsonOsActivity) {
    fun build(filters: MutableMap<String, Filter>): AlertListDialog {
        val options = ArrayList<DialogItem>()

        filters.forEach {
            options.add(this.getFilterItem(it.key, it.value))
        }

        return AlertListDialog(this.context, this.context.getString(R.string.filter), options)
    }

    private fun getFilterItem(key: String, filter: Filter): DialogItem {
        val dialogItem = DialogItem(filter.name)

        dialogItem.icon = R.drawable.ic_filter
        dialogItem.onClick = {
            OptionDialog(this.context).build(filter.name, filter.options).show()
        }

        return dialogItem
    }
}