package de.wollis_page.gibsonos.dto.response

import de.wollis_page.gibsonos.dto.response.filter.Option

data class Filter(
    val name: String,
    val multiple: Boolean,
    val options: MutableList<Option>,
)