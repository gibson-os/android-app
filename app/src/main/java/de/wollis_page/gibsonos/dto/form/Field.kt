package de.wollis_page.gibsonos.dto.form

data class Field(
    val title: String,
    val xtype: String,
    val config: Map<String, *> = mapOf<String, Any>(),
    val value: Any?,
    val subtext: String?,
    val image: String?,
    val operator: String?,
)
