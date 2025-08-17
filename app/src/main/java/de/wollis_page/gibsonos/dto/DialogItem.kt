package de.wollis_page.gibsonos.dto

import android.widget.ArrayAdapter
import de.wollis_page.gibsonos.helper.AlertListDialog

class DialogItem(val text: String, val id: Any? = null) {
    var onClick: ((flattedItem: FlattedDialogItem, adapter: ArrayAdapter<FlattedDialogItem>) -> Any?)? = null
    var icon: Int? = null
    var children: List<DialogItem>? = null
    var iconExpanded: Int? = null
    var fireOnClickOnExpand: Boolean = false
    var expanded: Boolean = false
    var scrollTo: Boolean = false
    var checkbox: Boolean = false
    var onClickCheckbox: ((flattedItem: FlattedDialogItem, adapter: ArrayAdapter<FlattedDialogItem>) -> Any?)? = null
    var selected: Boolean = false
}