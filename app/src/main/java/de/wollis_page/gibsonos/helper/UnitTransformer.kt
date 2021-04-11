package de.wollis_page.gibsonos.helper

fun Long.toHumanReadableByte(): String {
    val units = listOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    val valuePerUnit = 1024
    var pointer = 0
    var value = this.toFloat()

    while (value > valuePerUnit) {
        value /= valuePerUnit
        pointer++
    }

    return "%.2f".format(value) + " " + units[pointer]
}