package de.wollis_page.gibsonos.module.growDiary.index.dto.setup

import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.module.growDiary.index.dto.Light

class Light(
    var id: Long,
    var light: Light,
): ListItemInterface {
    override fun getId() = this.id
}
