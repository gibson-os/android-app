package de.wollis_page.gibsonos.dto.message

data class Button(
    val text: String,
    val value: String?,
    val parameter: Map<String, *>?,
    val sendRequest: Boolean,
)
