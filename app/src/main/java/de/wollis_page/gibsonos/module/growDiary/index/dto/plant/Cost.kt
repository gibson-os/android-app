package de.wollis_page.gibsonos.module.growDiary.index.dto.plant

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.enum.plant.CostType

data class Cost(
    @get:JvmName("getOriginalId") var id: String,
    var foreignId: Long,
    var name: String,
    var costPerUnit: Float?,
    var units: Float?,
    var totalCost: Float,
    var type: CostType
): ListItemInterface {
    override fun getId() = this.id
}
