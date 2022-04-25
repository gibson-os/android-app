package de.wollis_page.gibsonos.module.hc.neopixel.dto

data class Pixel(
    val number: Int,
    val left: Int,
    val top: Int,
    val channel: Int,
    var red: Int,
    var green: Int,
    var blue: Int,
    var fadeIn: Int,
    var blink: Int,
)
