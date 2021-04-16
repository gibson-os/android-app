package de.wollis_page.gibsonos.module.core.cronjob.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

class Cronjob(
    val id: Int,
    val command: String,
    val lastRun: String?
): ListItemInterface