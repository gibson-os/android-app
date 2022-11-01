package de.wollis_page.gibsonos.module.explorer.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import java.io.Serializable

data class Item(
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
    var metaInfos: Map<String, Any>?,
    val html5VideoStatus: Html5Status?,
    val html5VideoToken: String?,
    val thumbAvailable: Boolean = false,
    val position: Int? = null
): ListItemInterface, Serializable {
    override fun getId(): String = this.path.toString() + this.name
}