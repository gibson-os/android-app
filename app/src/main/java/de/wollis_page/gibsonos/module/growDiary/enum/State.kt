package de.wollis_page.gibsonos.module.growDiary.enum

import com.squareup.moshi.Json

enum class State {
    @Json(name="NONE")
    NONE,
    @Json(name="GERMINATION")
    GERMINATION,
    @Json(name="GROWTH")
    GROWTH,
    @Json(name="FLOWERING")
    FLOWERING,
    @Json(name="DRYING")
    DRYING,
    @Json(name="FINISHED")
    FINISHED,
}