package de.wollis_page.gibsonos.dto

import kotlin.reflect.KClass

data class Update(
    val module: String,
    val task: String,
    val action: String,
    val foreignId: String,
    val dtoClass: KClass<*>
)
