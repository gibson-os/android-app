package de.wollis_page.gibsonos.module.obscura.template.dto

import de.wollis_page.gibsonos.module.obscura.scanner.dto.Format

data class Template(
    val id: Long,
    val vendor: String,
    val model: String,
    val name: String,
    val path: String,
    val filename: String,
    val multipage: Boolean,
    val format: Format,
    val options: Map<String, *> = mapOf<String, Any>(),
)
