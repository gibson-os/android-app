package de.wollis_page.gibsonos.module.core.event.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Event(
    val id: Long,
    val name: String,
    val lastRun: String?
): ListItemInterface {
    override fun getId() = this.id
}