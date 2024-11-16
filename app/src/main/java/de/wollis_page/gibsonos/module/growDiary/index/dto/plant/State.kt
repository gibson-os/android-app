package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.module.growDiary.enum.State

data class State(
    var state: State,
    var title: String,
    var from: String?,
    var to: String?,
    var days: Int,
    var week: Int,
    var fromDaysSinceStart: Int,
    var toDaysSinceStart: Int,
    var fromWeekSinceStart: Int,
    var toWeekSinceStart: Int,
    var grownAt: String?,
)
