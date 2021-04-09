package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.util.Log
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.adapter.DesktopAdapter
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.task.DesktopTask

class DesktopActivity : ListActivity() {
    private lateinit var adapter: DesktopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_desktop)
        super.onCreate(savedInstanceState)

        this.adapter = DesktopAdapter(this.applicationContext, this.getAccount())
        this.listView.adapter = this.adapter

        this.loadDesktop()
    }

    private fun loadDesktop() = this.load {
        val desktop = DesktopTask.index(this, it.account)

        it.apps = desktop.apps
        this.adapter.desktop = desktop.desktop
        Log.d(Config.LOG_TAG, desktop.apps.size.toString())
        this.loadNavigation()
    }
}