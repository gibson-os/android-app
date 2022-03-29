package de.wollis_page.gibsonos.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import kotlin.reflect.full.createInstance

class BaseTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private var tabs: MutableList<Tab> = ArrayList()
    var fragments: MutableList<GibsonOsFragment> = ArrayList()

    override fun getItemCount(): Int {
        return this.tabs.count()
    }

    override fun createFragment(position: Int): GibsonOsFragment {
        val tab = this.tabs.get(position)
        val fragment = tab.className.createInstance() as GibsonOsFragment
        fragment.arguments = Bundle().apply {
            putSerializable("arguments", tab.arguments)
        }
        this.fragments.add(fragment)

        return fragment
    }

    fun addTab(tab: Tab) {
        this.tabs.add(tab)
    }
}