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
    fun build(filters: MutableMap<String, Filter>, selectedFilters: MutableMap<String, MutableList<String>>): AlertListDialog {
        val options = ArrayList<DialogItem>()

        filters.forEach {
            val filterItem = this.getFilterItem(it.key, it.value, selectedFilters)

            options.add(filterItem)
        }

        return AlertListDialog(
            this.context,
            this.context.getString(R.string.filter),
            options,
            DialogButton("Anwenden") {
                val checkedFilters: MutableMap<String, MutableList<String>> = mutableMapOf()

                options.forEach { filter ->
                    if (filter.selected) {
                        return@forEach
                    }

                    val selectedFilter = filters.firstNotNullOf {
                        filterItem -> if (filterItem.value.name == filter.text) filterItem else null
                    }
                    checkedFilters[selectedFilter.key] = mutableListOf()

                    filter.children?.forEach {
                        try {
                            val option = selectedFilter.value.options.firstNotNullOf { option ->
                                if (option.value == it.id && it.selected) option else null
                            }

                            checkedFilters[selectedFilter.key]?.add(option.value.toString())
                        } catch (_: NoSuchElementException) {
                        }
                    }
                }

                this.run(checkedFilters)
            },
            DialogButton("Abbrechen") {
            }
        )
    }

    private fun getFilterItem(key: String, filter: Filter, selectedFilters: MutableMap<String, MutableList<String>>): DialogItem {
        val dialogItem = DialogItem(filter.name, key)
        val selectedItemFilter = selectedFilters[key] ?: mutableListOf()
        val selectedItemFilterCount = selectedItemFilter.count()

        dialogItem.icon = R.drawable.ic_filter_menu
        dialogItem.expanded = true
        dialogItem.fireOnClickOnExpand = false
        dialogItem.children = this.getOptionItems(filter, dialogItem, selectedItemFilter)
        dialogItem.checkbox = true
        dialogItem.selected = false

        if (
            selectedItemFilterCount == 0 ||
            selectedItemFilterCount == filter.options.count()
        ) {
            dialogItem.selected = true
            dialogItem.expanded = false
        }

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

    private fun getOptionItems(filter: Filter, parent: DialogItem, selectedFilters: MutableList<String>): MutableList<DialogItem> {
        val children = mutableListOf<DialogItem>()

        filter.options.forEach {
            val dialogItem = DialogItem(it.name, it.value)
            dialogItem.icon = R.drawable.ic_filter_menu
            dialogItem.checkbox = true
            dialogItem.selected = false

            if (
                selectedFilters.count() == 0 ||
                selectedFilters.contains(it.value.toString())
            ) {
                dialogItem.selected = true
            }

            val onClick: ((flattedItem: FlattedDialogItem, adapter: ArrayAdapter<FlattedDialogItem>) -> Any?) = { flattedItem, adapter ->
                var allSelected = true

                parent.children?.forEach loop@{ children ->
                    if (!children.selected) {
                        allSelected = false

                        return@loop
                    }
                }
                parent.selected = allSelected

                adapter.notifyDataSetChanged()
            }
            dialogItem.onClick = onClick
            dialogItem.onClickCheckbox = onClick

            children.add(dialogItem)
        }

        return children
    }
}