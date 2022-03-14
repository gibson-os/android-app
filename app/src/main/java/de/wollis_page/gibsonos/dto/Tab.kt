package de.wollis_page.gibsonos.dto

import java.io.Serializable
import kotlin.reflect.KClass

data class Tab(
    val title: String,
    val className: KClass<*>,
    val arguments: Map<String, *> = emptyMap<String, Any>()
): Serializable
