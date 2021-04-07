package de.wollis_page.gibsonos.activity.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.orm.SugarRecord
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.DesktopActivity
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account

abstract class GibsonOsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var account: Account? = null
        private set
    protected lateinit var application: GibsonOsApplication
    protected lateinit var accountMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = intent
        this.application = getApplication() as GibsonOsApplication

        if (intent.hasExtra(Account.EXTRA_ACCOUNT)) {
            this.account = intent.getParcelableExtra(Account.EXTRA_ACCOUNT)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        loadNavigation()

        if (this.account != null) {
            this.application.addProcess(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(Config.LOG_TAG, "onNavigationItemSelected: ")
        runActivity(DesktopActivity::class.java, SugarRecord.findById<Account>(Account::class.java, item.groupId))
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    protected fun runActivity(activity: Class<*>?, account: Account? = this.account) {
        val intent = Intent(this, activity)
        intent.putExtra(ACCOUNT_KEY, account)
        finish()
        this.startActivity(intent)
    }

    fun loadNavigation() {
        val navigationView = this.findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        this.accountMenu = navigationView.getMenu()
        val accounts = this.application.getAccounts()

        for (account in accounts) {
            this.accountMenu.add(account.account.id.toInt(), Menu.NONE, Menu.NONE, account.account.alias)
            var subMenuId = account.account.id.toInt() * 1000
            Log.d(Config.LOG_TAG, "Add menu")

            for (app in account.apps) {
                Log.d(Config.LOG_TAG, "Add submenu")
                this.accountMenu.addSubMenu(subMenuId++, Menu.NONE, Menu.NONE, app.text)
            }
        }
    }

    companion object {
        const val ACCOUNT_KEY = "account"
    }
}