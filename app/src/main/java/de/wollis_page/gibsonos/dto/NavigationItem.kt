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

    fun isShortcut() = this.app === null && this.shortcut !== null

    fun getText() = this.app?.text ?: this.shortcut?.text ?: this.account.account.alias ?: ""
    fun getModule() = this.app?.module ?: this.shortcut?.module ?: "core"

    fun getTask() = this.app?.task ?: this.shortcut?.task ?: "index"

    fun getAction() = this.app?.action ?: this.shortcut?.action ?: "index"
}
