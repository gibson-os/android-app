package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.module.core.desktop.dto.App
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut

data class NavigationItem(
    val account: Account,
    val app: App? = null,
    val shortcut: Shortcut? = null,
) {
    fun isAccount() = this.app === null && this.shortcut === null

    fun isApp() = this.app !== null && this.shortcut === null

    fun isShortcut() = this.shortcut !== null

    fun getText() = this.shortcut?.text ?: this.app?.text ?: this.account.account.alias ?: ""

    fun getModule() = this.shortcut?.module ?: this.app?.module ?: "core"

    fun getTask() = this.shortcut?.task ?: this.app?.task ?: "index"

    fun getAction() = this.shortcut?.action ?: this.app?.action ?: "index"
}
