package de.wollis_page.gibsonos.dto

data class ListResponse<E>(
    val data: MutableList<E>,
    val total: Long,
    val start: Long,
    val limit: Long
)
