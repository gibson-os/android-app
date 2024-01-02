package de.wollis_page.gibsonos.module.obscura.scanner.dto

import com.squareup.moshi.Json

enum class Format {
    @Json(name="PDF")
    PDF,
    @Json(name="PDF_DUPLEX")
    PDF_DUPLEX,
    @Json(name="TIFF")
    TIFF,
    @Json(name="JPEG")
    JPEG,
    @Json(name="PNG")
    PNG,
}