package de.wollis_page.gibsonos.module.obscura.scanner.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Scanner(
    val deviceName: String,
    val vendor: String,
    val model: String,
    val type: String,
    val index: Long,
): ListItemInterface {
    override fun getId(): String = this.deviceName
}
