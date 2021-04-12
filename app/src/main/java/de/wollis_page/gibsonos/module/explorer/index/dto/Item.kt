package de.wollis_page.gibsonos.module.explorer.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

class Item(
    val name: String,
    val size: Long,
    val path: String?,
    val type: String,
    val dirs: Long?,
    val files: Long?,
    val dirDirs: Long?,
    val dirFiles: Long?,
    val icon: String?,
    val category: Int?,
    val metaInfos: Map<String, Any>?,
    val html5VideoStatus: String?,
    val html5VideoToken: String?,
    val thumbAvailable: Boolean = false
): ListItemInterface