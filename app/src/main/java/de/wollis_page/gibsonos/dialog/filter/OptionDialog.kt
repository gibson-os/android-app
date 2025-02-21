package de.wollis_page.gibsonos.dialog.filter

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.response.filter.Option
import de.wollis_page.gibsonos.helper.AlertListDialog

class OptionDialog(private val context: GibsonOsActivity) {
    fun build(name: String, filterOptions: MutableList<Option>): AlertListDialog {
        val options = ArrayList<DialogItem>()

        filterOptions.forEach {
            options.add(this.getOptionItem(it))
        }

        return AlertListDialog(this.context, name, options)
    }

    private fun getOptionItem(option: Option): DialogItem {
        val dialogItem = DialogItem(option.name)

        dialogItem.icon = R.drawable.ic_filter
        dialogItem.onClick = {
        }

        return dialogItem
    }
}