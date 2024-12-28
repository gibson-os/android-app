package de.wollis_page.gibsonos.fragment

import androidx.recyclerview.widget.GridLayoutManager
import de.wollis_page.gibsonos.R

abstract class GridFragment: ListFragment() {
    override fun getLayoutManager() = GridLayoutManager(this.activity, 3)

    override fun getContentView() = R.layout.base_list
}