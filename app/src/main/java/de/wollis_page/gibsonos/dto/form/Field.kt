package de.wollis_page.gibsonos.dto.form

import com.squareup.moshi.Json

data class Field(
    val title: String,
    val xtype: String,
    val config: MutableMap<String, *> = mutableMapOf<String, Any>(),
    val value: Any?,
    @Json(name = "subText")
    val subtext: String?,
    val image: String?,
    val operator: String?,
    val submitOnChange: Map<String, *>? = null,
)
