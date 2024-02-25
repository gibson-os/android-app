package de.wollis_page.gibsonos.dto

import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.core.desktop.dto.App
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut

class Account(val account: Account) {
    private var navigationItems: MutableList<NavigationItem> = ArrayList()

    fun getSortedNavigationItems(): MutableList<NavigationItem> {
        val sortedNavigationItems = mutableListOf<NavigationItem>()

        for (app in this.getAppNavigationItems().sortedBy { it.getText() }) {
            sortedNavigationItems.add(app)

            for (shortcut in this.getShortcutNavigationItems(app).sortedBy { it.getText() }) {
                sortedNavigationItems.add(shortcut)
            }
        }

        return sortedNavigationItems
    }

    private fun getAppNavigationItems() = this.navigationItems.filter { it.isApp() }

    private fun getShortcutNavigationItems(navigationItem: NavigationItem) = this.navigationItems.filter {
        it.isShortcut() &&
        it.getModule() == navigationItem.getModule() &&
        it.getTask() == navigationItem.getTask() &&
        it.getAction() == navigationItem.getAction()
    }

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
            (it.shortcut?.parameters ?: mutableMapOf()) == (navigationItem.shortcut?.parameters ?: mutableMapOf<String, Any>())
        }

        if (foundNavigationItem !== null) {
            return foundNavigationItem
        }

        this.navigationItems.add(navigationItem)

        return navigationItem
    }

    fun removeNavigationItem(navigationItem: NavigationItem) {
        this.navigationItems.remove(navigationItem)
    }
}