package de.wollis_page.gibsonos.module.explorer.dto

import de.wollis_page.gibsonos.dto.ListInterface

class Item(
    val name: String,
    val size: Long,
    val path: String?,
    val type: String,
    val dirs: Long?,
    val files: Long?,
    val dirDirs: Long?,
    val dirFiles: Long?,
    val icon: String?
): ListInterface