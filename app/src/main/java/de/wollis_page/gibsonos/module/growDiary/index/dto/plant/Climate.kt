package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Climate(
    var id: Long,
    var added: String,
    var temperature: Float?,
    var relativeHumidity: Float?,
    var airPressure: Float?,
    var leafTemperature: Float?,
): ListItemInterface {
    override fun getId() = this.id
}
