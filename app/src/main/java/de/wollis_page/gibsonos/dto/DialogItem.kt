package de.wollis_page.gibsonos.dto

class DialogItem(val text: String) {
    var onClick: ((flattedItem: FlattedDialogItem) -> Unit?)? = null
    var icon: Int? = null
    var children: List<DialogItem>? = null
    var iconExpanded: Int? = null
    var fireOnClickOnExpand: Boolean = false
    var expanded: Boolean = false
    var scrollTo: Boolean = false
}