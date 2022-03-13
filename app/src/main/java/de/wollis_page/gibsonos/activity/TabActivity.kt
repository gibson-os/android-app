package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseTabAdapter


abstract class TabActivity : GibsonOsActivity() {
    protected lateinit var adapter: BaseTabAdapter

    abstract fun getTabs(): Array<String>

    override fun getContentView() = R.layout.base_tab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        this.adapter = BaseTabAdapter(this)

        for (tab in this.getTabs()) {
            this.adapter.addTab(tab)
        }

        val viewPager = findViewById<View>(R.id.content) as ViewPager2
        viewPager.adapter = this.adapter

        val tabLayout = findViewById<View>(R.id.tabs) as TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "OBJECT ${(position + 1)}"
        }.attach()
    }
}