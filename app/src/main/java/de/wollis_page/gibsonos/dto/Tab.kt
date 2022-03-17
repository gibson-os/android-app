package de.wollis_page.gibsonos.dto

import java.io.Serializable
import kotlin.reflect.KClass

data class Tab(
    val title: Int,
    val className: KClass<*>,
    val arguments: HashMap<String, *> = hashMapOf<String, Any>()
): Serializable
