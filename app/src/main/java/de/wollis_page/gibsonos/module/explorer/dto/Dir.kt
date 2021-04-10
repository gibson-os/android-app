package de.wollis_page.gibsonos.module.explorer.dto

import de.wollis_page.gibsonos.dto.ListInterface

class Dir(
    val data: MutableList<Item>,
    val dir: String,
    val homePath: String,
    val total: Long
)