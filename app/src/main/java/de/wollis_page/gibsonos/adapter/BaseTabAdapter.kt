package de.wollis_page.gibsonos.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.wollis_page.gibsonos.dto.Tab
import kotlin.reflect.full.createInstance

class BaseTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var tabs: MutableList<Tab> = ArrayList()

    override fun getItemCount(): Int {
        return this.tabs.count()
    }

    override fun createFragment(position: Int): Fragment {
        val tab = this.tabs.get(position)
        val fragment = tab.className.createInstance() as Fragment
        fragment.arguments = Bundle().apply {
            putSerializable("tab", tab)
        }

        return fragment
    }

    fun addTab(tab: Tab) {
        this.tabs.add(tab)
    }
}