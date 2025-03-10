package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

data class Climate(
    var measuringPoint: String,
    var minTemperature: Float?,
    var maxTemperature: Float?,
    var minRelativeHumidity: Float?,
    var maxRelativeHumidity: Float?,
    var minAirPressure: Float?,
    var maxAirPressure: Float?,
    var minLeafTemperature: Float?,
    var maxLeafTemperature: Float?,
)
