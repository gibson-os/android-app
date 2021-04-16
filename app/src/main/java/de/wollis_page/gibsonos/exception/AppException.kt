package de.wollis_page.gibsonos.exception

class AppException(
    override val message: String,
    messageRessource: Int? = null
) : MessageException(messageRessource)