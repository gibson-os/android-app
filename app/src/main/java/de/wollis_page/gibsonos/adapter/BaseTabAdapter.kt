package de.wollis_page.gibsonos.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var fragments: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return this.fragments.count()
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = Class.forName(fragments.get(position)) as Fragment
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
        }

        return fragment
    }

    fun addTab(fragementClassName: String) {
        this.fragments.add(fragementClassName)
    }
}