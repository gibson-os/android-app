package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.core.desktop.dto.App
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut

class Account(val account: Account) {
    var navigationItems: MutableList<NavigationItem> = ArrayList()

    fun addNavigationItem(app: App) = this.addNavigationItem(NavigationItem(
        this,
        app,
    ))

    fun addNavigationItem(shortcut: Shortcut) = this.addNavigationItem(NavigationItem(
        this,
        null,
        shortcut,
    ))
    private fun addNavigationItem(navigationItem: NavigationItem): NavigationItem {
        val foundNavigationItem = this.navigationItems.find {
            it.getModule() == navigationItem.getModule() &&
            it.getTask() == navigationItem.getTask() &&
            it.getAction() == navigationItem.getAction() &&
            it.shortcut?.parameters == navigationItem.shortcut?.parameters
        }

        if (foundNavigationItem !== null) {
            return foundNavigationItem;
        }

        this.navigationItems.add(navigationItem)

        return navigationItem
    }

    fun removeNavigationItem(navigationItem: NavigationItem) {
        this.navigationItems.remove(navigationItem)
    }
}