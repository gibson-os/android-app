package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Setup
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.State
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Climate as PlantClimate

data class Plant(
    var id: Long,
    var name: String,
    var seed: Seed,
    var state: State?,
    var states: Map<String, State>?,
    var lastClimates: List<Climate>?,
    var climates: List<PlantClimate>?,
    var lastFeed: Feed?,
    var sumFeed: Feed?,
    var lastMilestone: Milestone?,
    var minRemainingGrowingDays: Int?,
    var maxRemainingGrowingDays: Int?,
    var minRemainingGrowingWeeks: Int?,
    var maxRemainingGrowingWeeks: Int?,
    var minRemainingGrowingDaysOtherPlants: Int?,
    var maxRemainingGrowingDaysOtherPlants: Int?,
    var minRemainingGrowingWeeksOtherPlants: Int?,
    var maxRemainingGrowingWeeksOtherPlants: Int?,
    var harvestedWet: Float?,
    var harvestedDry: Float?,
    var currentSetup: Setup?
): ListItemInterface {
    override fun getId() = this.id
}
