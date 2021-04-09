package de.wollis_page.gibsonos.module.explorer.dto

class Dir(
    val data: MutableList<Item>,
    val dir: String,
    val homePath: String,
    val total: Long
)