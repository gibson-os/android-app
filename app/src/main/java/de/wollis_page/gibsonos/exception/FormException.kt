package de.wollis_page.gibsonos.exception

class FormException (
    override val message: String,
    messageRessource: Int? = null
) : MessageException(messageRessource)