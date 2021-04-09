package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.adapter.DesktopAdapter
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.task.DesktopTask
import java.util.concurrent.CompletableFuture

class DesktopActivity : ListActivity() {
    private lateinit var adapter: DesktopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_desktop)
        super.onCreate(savedInstanceState)

        this.adapter = DesktopAdapter(this.applicationContext, this.getAccount())
        this.listView.adapter = this.adapter

        this.loadDesktop()
    }

    private fun loadDesktop() {
        val me = this
        val account = this.getAccount()

        CompletableFuture.supplyAsync<Any> {
            val accountModel = me.application.getAccountById(account.id)

            if (accountModel === null) {
                me.runOnUiThread {
                    Toast.makeText(me, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    me.finish()
                }

                return@supplyAsync
            }

            try {
                val desktop = DesktopTask.index(me, account)

                accountModel.apps = desktop.apps
                me.adapter.desktop = desktop.desktop
                Handler(Looper.getMainLooper()).post { adapter.notifyDataSetChanged() }
                Log.d(Config.LOG_TAG, desktop.apps.size.toString())
                me.loadNavigation()
            } catch (exception: TaskException) {
                me.runOnUiThread {
                    var message = exception.message
                    val messageRessource = exception.messageRessource

                    if (messageRessource != null) {
                        message = getString(messageRessource)
                    }

                    Toast.makeText(me, message, Toast.LENGTH_LONG).show()
                    me.finish()
                }
            }

            return@supplyAsync
        }.exceptionally { e -> e.printStackTrace() }
    }
}