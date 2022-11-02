package de.wollis_page.gibsonos.module.explorer.index.dto

data class ConvertStatus(
    val status: Html5Status,
    val frame: Int?,
    val fps: Int?,
    val quality: Float?,
    val size: Int?,
    val time: String?,
    val bitrate: Float?,
    val frames: Int = 0
)
