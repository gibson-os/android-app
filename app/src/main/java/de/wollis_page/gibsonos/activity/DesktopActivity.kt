package de.wollis_page.gibsonos.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.AppCompatListActivity
import de.wollis_page.gibsonos.adapter.DesktopAdapter
import de.wollis_page.gibsonos.task.DesktopTask.index
import java.util.concurrent.CompletableFuture

class DesktopActivity : AppCompatListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_desktop)
        super.onCreate(savedInstanceState)
        val activity = this
        CompletableFuture.supplyAsync<Any> {
            val desktop = index(activity, activity.account!!)
            val desktopAdapter = DesktopAdapter(activity.applicationContext, desktop!!.desktop)
            activity.setListAdapter(desktopAdapter)
            null
        }
    }
}