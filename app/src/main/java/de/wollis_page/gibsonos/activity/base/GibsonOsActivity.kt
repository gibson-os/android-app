package de.wollis_page.gibsonos.activity.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.orm.SugarRecord
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.DesktopActivity
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.dto.desktop.Item
import de.wollis_page.gibsonos.exception.AccountException
import de.wollis_page.gibsonos.exception.ActivityException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account

abstract class GibsonOsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var account: Account? = null
    private var item: Item? = null
    protected lateinit var application: GibsonOsApplication
    protected lateinit var contentContainer: ConstraintLayout
    private lateinit var navigationView: NavigationView
    private lateinit var progressBarHolder: FrameLayout

    abstract protected fun getContentView(): Int

    @Throws(Exception::class)
    fun getAccount(): Account {
        val account = this.account

        if (account == null) {
            Toast.makeText(this, R.string.account_error_not_exists, Toast.LENGTH_LONG).show()

            throw AccountException("No account set!")
        }

        return account
    }

    fun getItem(): Item {
        val item = this.item

        if (item == null) {
            Toast.makeText(this, R.string.no_item_set, Toast.LENGTH_LONG).show()

            throw ActivityException("No Item set!")
        }

        return item
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setContentView(R.layout.base_layout)
        super.onCreate(savedInstanceState)
        val intent = intent
        this.application = getApplication() as GibsonOsApplication

        this.contentContainer = this.findViewById(R.id.content) as ConstraintLayout
        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            this.getContentView(),
            this.findViewById(android.R.id.content),
            false
        ))

        if (intent.hasExtra(ACCOUNT_KEY)) {
            this.account = intent.getParcelableExtra(ACCOUNT_KEY)
        }

        if (intent.hasExtra(ITEM_KEY)) {
            this.item = intent.getParcelableExtra(ITEM_KEY)
        }

        val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
        val drawer = this.findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        this.navigationView = this.findViewById(R.id.nav_view)
        this.navigationView.setNavigationItemSelectedListener(this)
        loadNavigation()

        this.progressBarHolder = this.findViewById(R.id.progressBarHolder)

        if (this.account != null) {
            this.application.addProcess(this)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(Config.LOG_TAG, "onNavigationItemSelected: ")
        runActivity(DesktopActivity::class.java, SugarRecord.findById(Account::class.java, item.groupId))
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun runActivity(activity: Class<*>?, account: Account? = this.account) {
        val intent = Intent(this, activity)
        intent.putExtra(ACCOUNT_KEY, account)
        finish()
        this.startActivity(intent)
    }

    fun loadNavigation() {
        Handler(Looper.getMainLooper()).post {
            val menu = navigationView.menu
            menu.clear()
            val accounts = this.application.accounts

            for (account in accounts) {
                if (account.apps.size == 0) {
                    menu.add(account.account.id.toInt(), Menu.NONE, Menu.NONE, account.account.alias)
                    continue
                }

                val accountMenu = menu.addSubMenu(account.account.id.toInt(), Menu.NONE, Menu.NONE, account.account.alias)
                Log.d(Config.LOG_TAG, "Add submenu " + account.account.alias)
                Log.d(Config.LOG_TAG, account.apps.size.toString())

                for (app in account.apps) {
                    Log.d(Config.LOG_TAG, "Add task")
                    accountMenu.addSubMenu(app.text)
                }
            }
        }
    }

    fun showLoading() {
        val inAnimation = AlphaAnimation(0.0f, 1.0f)
        inAnimation.duration = 200

        Handler(Looper.getMainLooper()).post {
            this.progressBarHolder.animation = inAnimation
            this.progressBarHolder.visibility = View.VISIBLE
        }
    }

    fun hideLoading() {
        val outAnimation = AlphaAnimation(1.0f, 0.0f)
        outAnimation.duration = 200

        Handler(Looper.getMainLooper()).post {
            this.progressBarHolder.animation = outAnimation
            this.progressBarHolder.visibility = View.GONE
        }
    }

    companion object {
        const val ACCOUNT_KEY = "account"
        const val ITEM_KEY = "item"
    }
}