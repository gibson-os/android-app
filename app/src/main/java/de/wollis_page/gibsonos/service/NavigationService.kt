package de.wollis_page.gibsonos.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.activity.SettingActivity
import de.wollis_page.gibsonos.helper.Config

class NavigationService(private val context: GibsonOsActivity): NavigationView.OnNavigationItemSelectedListener {
    private val navigationAccountGroupBit = 0
    private val navigationAppGroupBit = 1
    private val navigationProcessGroupBit = 2
    private lateinit var navigationView: NavigationView

    fun create() {
        this.navigationView = this.context.findViewById(R.id.nav_view)
        this.navigationView.setNavigationItemSelectedListener(this)

        val setting = this.context.findViewById<ImageView>(R.id.setting)
        setting.setOnClickListener {
            this.context.finish()
            this.context.startActivity(Intent(this.context, SettingActivity::class.java))
            this.context.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(Config.LOG_TAG, "onNavigationItemSelected: " + item.itemId)

        val accountId = item.groupId shr 2
        val account = this.context.application.accounts[accountId]

        val drawer = this.context.findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)

        try {
            when (item.groupId and 3) {
                this.navigationAccountGroupBit -> {
                    Log.d(Config.LOG_TAG, "Navigation account")
                    ActivityLauncherService.startActivity(
                        this.context,
                        "core",
                        "desktop",
                        "index",
                        account.account.id,
                        emptyMap(),
                        account
                    )
                }
                this.navigationAppGroupBit -> {
                    Log.d(Config.LOG_TAG, "Navigation app")
                    val app = account.apps[item.itemId]
                    ActivityLauncherService.startActivity(
                        this.context,
                        app.module,
                        app.task,
                        app.action,
                        0,
                        emptyMap(),
                        account
                    )
                }
                this.navigationProcessGroupBit -> {
                    Log.d(Config.LOG_TAG, "Navigation proccess")
                    val activity = account.getProcesses()[item.itemId].activity
                    val activityManager = activity.applicationContext.getSystemService(
                        Context.ACTIVITY_SERVICE
                    ) as ActivityManager
                    activityManager.moveTaskToFront(activity.taskId, 0)
                }
            }
        } catch (exception: ClassNotFoundException) {
            Toast.makeText(this.context, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
        }

        return true
    }

    fun loadNavigation() {
        this.context.runOnUiThread {
            val menu = navigationView.menu
            menu.clear()
            val accounts = this.context.application.accounts

            for ((accountId, account) in accounts.withIndex()) {
                val accountGroupId = accountId shl 2
                val appGroupId = accountGroupId or this.navigationAppGroupBit
                val processGroupId = accountGroupId or this.navigationProcessGroupBit

                if (account.apps.size == 0) {
                    menu.add(accountGroupId, accountId, Menu.NONE, account.account.alias)
                    continue
                }

                val accountMenu = menu.addSubMenu(accountGroupId, accountId, Menu.NONE, account.account.alias)
                Log.d(Config.LOG_TAG, "Add submenu " + account.account.alias)
                Log.d(Config.LOG_TAG, account.apps.size.toString())

                for ((appId, app) in account.apps.withIndex()) {
                    Log.d(Config.LOG_TAG, "Add task")
                    val appItem = accountMenu.add(appGroupId, appId, Menu.NONE, app.text)
                    appItem.icon = ResourcesCompat.getDrawable(
                        this.context.resources,
                        AppIconService.getIcon(app.module, app.task, "index") ?: R.drawable.ic_android,
                        this.context.theme
                    )

                    for ((processId, process) in account.getProcesses().withIndex()) {
                        val activityName = this.context.application.getActivityName(app.module, app.task, app.action)
                        val activity = process.activity

                        if (activity::class.java.toString() == "class $activityName") {
                            Log.d(Config.LOG_TAG, "Add process")
                            val proccessItem = accountMenu.add(processGroupId, processId, Menu.NONE, activity.title)
                            proccessItem.icon = ResourcesCompat.getDrawable(
                                this.context.resources,
                                AppIconService.getIcon(activity) ?: R.drawable.ic_android,
                                this.context.theme
                            )
                        }
                    }
                }
            }
        }
    }
}