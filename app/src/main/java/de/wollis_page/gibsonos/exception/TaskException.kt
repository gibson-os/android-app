package de.wollis_page.gibsonos.exception

import org.json.JSONObject

class TaskException(override val message: String, val messageRessource: Int? = null) : Exception()