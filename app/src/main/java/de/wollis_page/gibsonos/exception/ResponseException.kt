package de.wollis_page.gibsonos.exception

class ResponseException(
    override val message: String,
    val response: String,
    val code: Int,
    messageRessource: Int? = null
) : MessageException(messageRessource)