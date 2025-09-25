package de.wollis_page.gibsonos.module.growDiary.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface

data class Fertilizer(
    var id: Long,
    var name: String,
    var pricePerLiter: Int,
    var manufacture: Manufacture?,
): ListItemInterface {
    override fun getId() = this.id
}
