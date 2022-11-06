package de.wollis_page.gibsonos.dto

class FlattedDialogItem(val dialogItem: DialogItem, val level: Int, val parent: FlattedDialogItem? = null) {
}