package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseTabAdapter
import de.wollis_page.gibsonos.dto.Tab


abstract class TabActivity : GibsonOsActivity() {
    private lateinit var adapter: BaseTabAdapter

    abstract fun getTabs(): Array<Tab>

    override fun getContentView() = R.layout.base_tab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        this.adapter = BaseTabAdapter(this)

        for (tab in this.getTabs()) {
            this.adapter.addTab(tab)
        }

        val viewPager = findViewById<View>(R.id.pager) as ViewPager2
        viewPager.adapter = this.adapter

        val tabLayout = findViewById<View>(R.id.tabLayout) as TabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(this.getTabs().get(position).title)
        }.attach()
    }

    override fun updateData(data: String) {
        this.adapter.fragments.forEach {
            it.updateData(data)
        }
    }
}