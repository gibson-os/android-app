package de.wollis_page.gibsonos.module.explorer.index.dto

data class DirList (
    val id: String,
    val text: String,
    var expanded: Boolean,
    var data: MutableList<DirList>?
)