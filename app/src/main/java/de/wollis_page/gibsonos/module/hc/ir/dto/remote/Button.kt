package de.wollis_page.gibsonos.module.hc.ir.dto.remote

data class Button(
    val id: Long,
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
    val eventId: Long?,
    val keys: MutableList<Key>,
)
