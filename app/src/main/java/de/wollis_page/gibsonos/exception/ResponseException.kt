package de.wollis_page.gibsonos.exception

import okhttp3.ResponseBody

class ResponseException(override val message: String, val response: String, private val code: Int) : Exception()