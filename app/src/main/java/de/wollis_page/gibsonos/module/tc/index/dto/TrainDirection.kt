package de.wollis_page.gibsonos.module.tc.index.dto

import com.squareup.moshi.Json

enum class TrainDirection {
    @Json(name="1")
    FORWARD,
    @Json(name="2")
    BACKWARD,
}