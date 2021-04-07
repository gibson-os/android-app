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
    protected var application: GibsonOsApplication? = null
    protected var navigationView: NavigationView? = null
    protected var accountMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = getApplication() as GibsonOsApplication
        val intent = intent
        account = null

        if (intent.hasExtra(Account.EXTRA_ACCOUNT)) {
            account = intent.getParcelableExtra(Account.EXTRA_ACCOUNT)
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = this.findViewById<NavigationView>(R.id.nav_view)
        Log.d(Config.LOG_TAG, "onCreate: before")
        navigationView.setNavigationItemSelectedListener(this)
        Log.d(Config.LOG_TAG, "onCreate: after")
        accountMenu = navigationView.getMenu()
        loadNavigation()

        if (account != null) {
            application!!.addProcess(this)
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

    private fun loadNavigation() {
        val accounts = application!!.accountModels

        for (account in accounts) {
            accountMenu!!.add(account.id.toInt(), Menu.NONE, Menu.NONE, account.alias)
            var subMenuId = account.id.toInt() * 1000

            for (app in account.apps) {
                accountMenu!!.addSubMenu(subMenuId++, Menu.NONE, Menu.NONE, app.text)
            }
        }
    }

    companion object {
        const val ACCOUNT_KEY = "account"
    }
}