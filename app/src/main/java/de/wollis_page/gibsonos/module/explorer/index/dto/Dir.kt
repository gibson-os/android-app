package de.wollis_page.gibsonos.module.explorer.index.dto

data class Dir(
        val data: MutableList<Item>,
        val dir: String,
        val homePath: String,
        val total: Long
)