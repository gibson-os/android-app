package de.wollis_page.gibsonos.module.hc.module.ir.dto.remote

import de.wollis_page.gibsonos.module.hc.module.ir.dto.Key

data class Key(
    val name: String,
    val top: Int,
    val left: Int,
    val width: Int,
    val height: Int,
    val borderTop: Boolean,
    val borderRight: Boolean,
    val borderBottom: Boolean,
    val borderLeft: Boolean,
    val borderRadiusTopLeft: Int,
    val borderRadiusTopRight: Int,
    val borderRadiusBottomLeft: Int,
    val borderRadiusBottomRight: Int,
    val eventId: Int?,
    val keys: MutableList<Key>,
)
