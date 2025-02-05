package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Climate(
    var id: Long,
    var measuringPoint: String,
    var added: String,
    var temperature: Float?,
    var relativeHumidity: Float?,
    var airPressure: Float?,
    var leafTemperature: Float?,
): ListItemInterface {
    override fun getId() = this.id
}
