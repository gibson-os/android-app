package de.wollis_page.gibsonos.exception

class ResponseException(
        override val message: String,
        val response: String,
        private val code: Int,
        override val messageRessource: Int? = null
) : MessageException()