package de.wollis_page.gibsonos.service

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.activity.SettingActivity
import de.wollis_page.gibsonos.adapter.NavigationAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.dto.NavigationItem
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut


class NavigationService(private val context: GibsonOsActivity) {
    private var navigationView: NavigationView = this.context.findViewById(R.id.navigation_view)
    private var navigationListView: ListView = this.context.findViewById(R.id.navigation_list_view)

    init {
        this.navigationListView.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, selectedView: View?, position: Int, id: Long) {
                val navigationItem = navigationListView.adapter.getItem(position) as NavigationItem
                val shortcut = navigationItem.shortcut

                if (shortcut !== null) {
                    Log.d(Config.LOG_TAG, "Navigation shortcut")
                    ActivityLauncherService.startActivity(
                        context,
                        shortcut,
                        mapOf(GibsonOsActivity.SHORTCUT_KEY to shortcut),
//                        null,
//                        shortcut.instanceState,
                    )

                    return
                }

                val account = navigationItem.account
                val app = navigationItem.app

                if (app !== null) {
                    Log.d(Config.LOG_TAG, "Navigation app")
                    ActivityLauncherService.startActivity(
                        context,
                        app.module,
                        app.task,
                        app.action,
                        mapOf(GibsonOsActivity.SHORTCUT_KEY to Shortcut(
                            app.module,
                            app.task,
                            app.action,
                            app.text,
                            "",
                            null,
                        )),
                        account,
                    )

                    return
                }

                Log.d(Config.LOG_TAG, "Navigation account")
                ActivityLauncherService.startActivity(
                    context,
                    "core",
                    "desktop",
                    "index",
                    emptyMap(),
                    account,
                )
            }
        }

        val setting = this.context.findViewById<ImageView>(R.id.setting)
        setting.setOnClickListener {
            this.context.finish()
            this.context.startActivity(Intent(this.context, SettingActivity::class.java))
            this.context.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        }
    }

    fun loadNavigation() {
        this.context.runOnUiThread {
            navigationView.menu.clear()
            val navigationItems = mutableListOf<NavigationItem>()

            for (account in this.context.application.accounts.sortedBy { it.account.alias ?: it.account.url }) {
                this.addAccount(navigationItems, account)
            }

            navigationListView.adapter = NavigationAdapter(this.context, navigationItems)
        }
    }

    private fun addAccount(navigationItems: MutableList<NavigationItem>, account: Account) {
        Log.d(Config.LOG_TAG, "Add navigation account: " + account.account.alias)

        navigationItems.add(NavigationItem(account))

        for (navigationItem in account.getSortedNavigationItems()) {
            navigationItems.add(navigationItem)
        }
    }
}