package de.wollis_page.gibsonos.dto

data class ListResponse<E>(
    val data: MutableList<E>,
    val total: Long? = null,
    val start: Long? = null,
    val limit: Long? = null
)
