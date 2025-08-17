package de.wollis_page.gibsonos.callback

interface MenuVisibilityCallback {
    fun updateFilterVisibility(visible: Boolean)
    fun updateSortVisibility(visible: Boolean)
}