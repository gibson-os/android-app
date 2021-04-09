package de.wollis_page.gibsonos.exception

import org.json.JSONObject

class TaskException(
    override val message: String,
    override val messageRessource: Int? = null
) : MessageException()