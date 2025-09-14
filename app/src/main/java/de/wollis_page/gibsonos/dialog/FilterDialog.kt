package de.wollis_page.gibsonos.dialog

import android.widget.ArrayAdapter
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.DialogButton
import de.wollis_page.gibsonos.dto.DialogItem
import de.wollis_page.gibsonos.dto.FlattedDialogItem
import de.wollis_page.gibsonos.dto.response.Filter
import de.wollis_page.gibsonos.helper.AlertListDialog

class FilterDialog(
    private val context: GibsonOsActivity,
    private val run: (selectedFilters: MutableMap<String, MutableList<String>>) -> Unit,
) {
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
                val selectedFilters: MutableMap<String, MutableList<String>> = mutableMapOf()

                options.forEach { filter ->
                    if (filter.selected) {
                        return@forEach
                    }

                    val selectedFilter = filters.firstNotNullOf {
                        filterItem -> if (filterItem.value.name == filter.text) filterItem else null
                    }
                    selectedFilters[selectedFilter.key] = mutableListOf()

                    filter.children?.forEach {
                        try {
                            val option = selectedFilter.value.options.firstNotNullOf { option ->
                                if (option.value == it.id && it.selected) option else null
                            }

                            selectedFilters[selectedFilter.key]?.add(option.value.toString())
                        } catch (_: NoSuchElementException) {
                        }
                    }
                }

                this.run(selectedFilters);
            },
            DialogButton("Abbrechen") {
            }
        )
    }

    private fun getFilterItem(key: String, filter: Filter): DialogItem {
        val dialogItem = DialogItem(filter.name, key)
        dialogItem.icon = R.drawable.ic_filter_menu
        dialogItem.expanded = false
        dialogItem.fireOnClickOnExpand = false
        dialogItem.children = this.getOptionItems(filter, dialogItem)
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

    private fun getOptionItems(filter: Filter, parent: DialogItem): MutableList<DialogItem> {
        val children = mutableListOf<DialogItem>()

        filter.options.forEach {
            val dialogItem = DialogItem(it.name, it.value)
            dialogItem.icon = R.drawable.ic_filter_menu
            dialogItem.checkbox = true
            dialogItem.selected = true

            children.add(dialogItem)
        }

        return children
    }
}