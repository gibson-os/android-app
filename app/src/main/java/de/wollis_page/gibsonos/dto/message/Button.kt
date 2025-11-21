package de.wollis_page.gibsonos.dto.message

data class Button(
    val text: String,
    val value: Any?,
    val parameter: String?,
    val sendRequest: Boolean,
)
