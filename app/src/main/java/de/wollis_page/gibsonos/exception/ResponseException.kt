package de.wollis_page.gibsonos.exception

import org.json.JSONObject

class ResponseException(override val message: String, val response: JSONObject, private val code: Int) : Exception()