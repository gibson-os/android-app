package de.wollis_page.gibsonos.exception

class TaskException(
    override val message: String,
    messageRessource: Int? = null
) : MessageException(messageRessource)