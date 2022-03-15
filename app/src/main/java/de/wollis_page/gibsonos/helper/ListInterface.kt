package de.wollis_page.gibsonos.helper

import android.view.View
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface

interface ListInterface {
    fun onClick(item: ListItemInterface)

    fun bind(item: ListItemInterface, view: View)

    fun getListRessource(): Int

    fun loadList(start: Long = 0, limit: Long = 100)

    fun getContentView() = R.layout.base_list
}