package de.wollis_page.gibsonos.module.growDiary.index.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.OverviewBuilderInterface

class OverviewModel: ViewModel() {
    private val liveItems = MutableLiveData<List<OverviewBuilderInterface>>()
    val items: LiveData<List<OverviewBuilderInterface>> = liveItems

    fun reset() {
        this.liveItems.value = null
    }

    fun addItem(item: OverviewBuilderInterface) {
        val loadedItems = this.liveItems.value?.toMutableList() ?: mutableListOf()

        loadedItems.add(item)
        this.liveItems.value = loadedItems
    }
}