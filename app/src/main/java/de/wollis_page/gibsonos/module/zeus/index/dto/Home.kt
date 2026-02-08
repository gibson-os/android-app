package de.wollis_page.gibsonos.module.zeus.index.dto

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.Manufacture

data class Home(
    var id: Long,
    var name: String,
    var residents: Int,
    var size: Int,
    var street: String,
    var streetNumber: String?,
    var postalCode: String?,
    var city: String?,
    var country: String?,
    var meteringPointEan: String?,
): ListItemInterface {
    override fun getId() = this.id
}
