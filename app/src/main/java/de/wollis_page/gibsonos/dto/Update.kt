package de.wollis_page.gibsonos.dto

data class Update(
    val module: String,
    val task: String,
    val action: String,
    val foreignId: String
)
