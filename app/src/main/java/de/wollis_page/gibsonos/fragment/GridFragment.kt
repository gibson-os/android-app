package de.wollis_page.gibsonos.fragment

import androidx.recyclerview.widget.GridLayoutManager

abstract class GridFragment: ListFragment() {
    override fun getLayoutManager() = GridLayoutManager(this.activity, 3)

    override fun getDefaultLimit(): Long = 15
}