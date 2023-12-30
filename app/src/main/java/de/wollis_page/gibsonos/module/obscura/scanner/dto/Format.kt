package de.wollis_page.gibsonos.module.obscura.scanner.dto

import com.squareup.moshi.Json

enum class Format {
    @Json(name="pdf")
    PDF,
    @Json(name="pdf (Duplex)")
    PDF_DUPLEX,
    @Json(name="tiff")
    TIFF,
    @Json(name="jpg")
    JPEG,
    @Json(name="png")
    PNG,
}