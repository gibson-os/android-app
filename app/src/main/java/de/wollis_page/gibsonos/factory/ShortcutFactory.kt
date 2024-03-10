package de.wollis_page.gibsonos.factory

import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.hc.index.dto.Module

object ShortcutFactory {
    fun create(module: Module): Shortcut {
        return Shortcut("hc", "module", "view", module.name, "", mutableMapOf(
            "id" to module.id,
            "type" to module.type,
            "name" to module.name,
            "address" to module.address,
            "helper" to module.helper,
            "modified" to module.modified,
        ))
    }
}