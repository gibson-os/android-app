package de.wollis_page.gibsonos.module.hc.ir.dto.remote

import de.wollis_page.gibsonos.module.hc.ir.dto.Key

data class Key(
    val id: Long,
    val order: Int,
    val key: Key,
)
