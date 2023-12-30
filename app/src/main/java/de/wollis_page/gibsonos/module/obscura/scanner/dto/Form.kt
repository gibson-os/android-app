package de.wollis_page.gibsonos.module.obscura.scanner.dto

data class Form(
    val deviceName: String,
    val vendor: String,
    val model: String,
    val name: String,
    val path: String,
    val filename: String,
    val multipage: Boolean,
    val format: Format,
    val options: HashMap<String, *> = hashMapOf<String, Any>(),
)
