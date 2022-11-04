package de.wollis_page.gibsonos.module.explorer.index.dto

import com.squareup.moshi.Json

enum class Category {
    @Json(name="1")
    IMAGE,
    @Json(name="2")
    VIDEO,
    @Json(name="3")
    PDF,
    @Json(name="4")
    AUDIO,
    @Json(name="5")
    OFFICE,
    @Json(name="6")
    ARCHIVE,
    @Json(name="7")
    BINARY,
    @Json(name="8")
    TEXT,
}