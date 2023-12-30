package de.wollis_page.gibsonos.dto.form

data class Button(
    val text: String,
    val module: String,
    val task: String,
    val action: String,
    val parameters: Map<String, *> = mapOf<String, Any>(),
)
