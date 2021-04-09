package de.wollis_page.gibsonos.module.explorer.dto

class Item(
    val name: String,
    val path: String?, // @todo path sollte nicht optional sein
    val size: Long,
    val type: String,
    val dirs: Long?,
    val files: Long?,
    val dirDirs: Long?,
    val dirFiles: Long?,
    val icon: String?
)