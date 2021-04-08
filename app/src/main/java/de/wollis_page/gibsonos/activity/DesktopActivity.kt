package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.adapter.DesktopAdapter
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.task.DesktopTask
import java.util.concurrent.CompletableFuture

class DesktopActivity : ListActivity() {
    lateinit var adapter: DesktopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_desktop)
        super.onCreate(savedInstanceState)

        this.adapter = DesktopAdapter(this.applicationContext, this.account!!)
        this.listView.adapter = this.adapter

        this.loadDesktop()
    }

    private fun loadDesktop() {
        val me = this
        val account = this.account

        CompletableFuture.supplyAsync<Any> {
            if (account === null) {
                return@supplyAsync
            }

            val accountModel = me.application.getAccountById(account.id)

            if (accountModel === null) {
                Toast.makeText(me, "Kein Account Model gefunden!", Toast.LENGTH_LONG).show()

                return@supplyAsync
            }

            val desktop = DesktopTask.index(me, account)

            if (desktop === null) {
                Toast.makeText(me, "Desktop konnte nicht geladen werden!", Toast.LENGTH_LONG).show()

                return@supplyAsync
            }

            accountModel.apps = desktop.apps
            me.adapter.desktop = desktop.desktop
            Handler(Looper.getMainLooper()).post { adapter.notifyDataSetChanged() }
            Log.d(Config.LOG_TAG, desktop.apps.size.toString())
            me.loadNavigation()

            return@supplyAsync
        }.exceptionally({ e -> e.printStackTrace() })
    }
}