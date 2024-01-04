package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.dto.message.Button

data class Message(
    val msg: String,
    val title: String,
    val type: Int,
    val buttons: List<Button>,
    val extraParameters: Map<String, Any>,
)
