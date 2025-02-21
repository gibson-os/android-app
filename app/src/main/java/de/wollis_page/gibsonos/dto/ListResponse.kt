package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.dto.response.Filter

data class ListResponse<E>(
    val data: MutableList<E>,
    val total: Long? = null,
    val start: Long? = null,
    val limit: Long? = null,
    val filters: MutableMap<String, Filter>? = null,
    val possibleOrders: MutableList<String>? = null,
)
