package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.State

data class Plant(
    var id: Long,
    var name: String,
    var seed: Seed,
    var state: State?,
    var states: Map<String, State>?,
    var lastClimate: Climate?,
    var lastFeed: Feed?,
    var sumFeed: Feed?,
    var lastMilestone: Milestone?,
    var minTemperature: Climate?,
    var maxTemperature: Climate?,
    var minRelativeHumidity: Climate?,
    var maxRelativeHumidity: Climate?,
    var minRemainingGrowingDays: Int?,
    var maxRemainingGrowingDays: Int?,
    var minRemainingGrowingWeeks: Int?,
    var maxRemainingGrowingWeeks: Int?,
): ListItemInterface {
    override fun getId() = this.id
}
