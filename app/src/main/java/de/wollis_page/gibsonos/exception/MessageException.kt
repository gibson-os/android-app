package de.wollis_page.gibsonos.exception


abstract class MessageException(val messageRessource: Int? = null): Throwable() {
}