package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.adapter.DesktopAdapter
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.task.DesktopTask
import java.util.concurrent.CompletableFuture

class DesktopActivity : ListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_desktop)
        super.onCreate(savedInstanceState)
        val me = this
        val adapter = DesktopAdapter(me.applicationContext)
        me.listView.adapter = adapter

        CompletableFuture.supplyAsync<Any> {
            val desktop = DesktopTask.index(me, me.account!!)

            me.application.getAccountById(me.account!!.id)!!.apps = desktop!!.apps
            adapter.desktop = desktop.desktop
            Log.d(Config.LOG_TAG, "notifyDataSetChanged")
            Handler(Looper.getMainLooper()).post { adapter.notifyDataSetChanged() }
            Log.d(Config.LOG_TAG, desktop.apps.size.toString())
            me.loadNavigation()
            null
        }.exceptionally({ e -> e.printStackTrace() })
    }
}