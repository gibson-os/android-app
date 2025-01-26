package de.wollis_page.gibsonos.module.core.cronjob.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

class Cronjob(
    val id: Long,
    val command: String,
    val lastRun: String?
): ListItemInterface {
    override fun getId() = this.id
}