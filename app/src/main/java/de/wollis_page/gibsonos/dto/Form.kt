package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.dto.form.Field

data class Form(
    val fields: Map<String, Field> = mapOf(),
    val buttons: Map<String, Button> = mapOf(),
)
