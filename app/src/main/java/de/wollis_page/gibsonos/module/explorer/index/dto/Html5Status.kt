package de.wollis_page.gibsonos.module.explorer.index.dto

import com.squareup.moshi.Json

enum class Html5Status {
    @Json(name="wait")
    WAIT,
    @Json(name="generate")
    GENERATE,
    @Json(name="generated")
    GENERATED,
    @Json(name="error")
    ERROR,
}