package de.wollis_page.gibsonos.activity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
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
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.dto.Update
import de.wollis_page.gibsonos.exception.AccountException
import de.wollis_page.gibsonos.exception.ActivityException
import de.wollis_page.gibsonos.exception.MessageException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.core.desktop.activity.IndexActivity
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.task.DeviceTask
import java.io.Serializable
import java.util.concurrent.CompletableFuture

abstract class GibsonOsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var account: Account? = null
    private var shortcut: Shortcut? = null
    lateinit var application: GibsonOsApplication
    lateinit var contentContainer: ConstraintLayout
    private lateinit var navigationView: NavigationView
    private lateinit var progressBarHolder: FrameLayout
    var update: Update? = null

    protected abstract fun getContentView(): Int

    abstract fun getId(): Any

    open fun updateData(data: String) {
    }

    @Throws(Exception::class)
    fun getAccount(): Account {
        val account = this.account

        if (account == null) {
            Toast.makeText(this, R.string.account_error_not_exists, Toast.LENGTH_LONG).show()

            throw AccountException("No account set!")
        }

        return account
    }

    fun getShortcut(): Shortcut {
        val shortcut = this.shortcut

        if (shortcut == null) {
            Toast.makeText(this, R.string.no_shortcut_set, Toast.LENGTH_LONG).show()

            throw ActivityException("No Item set!")
        }

        return shortcut
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setContentView(R.layout.base_layout)
        super.onCreate(savedInstanceState)
        val intent = this.intent
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

        if (intent.hasExtra(SHORTCUT_KEY)) {
            this.shortcut = intent.getParcelableExtra(SHORTCUT_KEY)
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

        val setting = this.findViewById<ImageView>(R.id.setting)
        setting.setOnClickListener {
            finish()
            this.startActivity(Intent(this, SettingActivity::class.java))
            findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(Config.LOG_TAG, "onNavigationItemSelected: ")
        runActivity(IndexActivity::class.java, SugarRecord.findById(Account::class.java, item.groupId))
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun runActivity(activity: Class<*>?, account: Account? = this.account) {
        val intent = Intent(this, activity)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        intent.putExtra(ACCOUNT_KEY, account)
        finish()
        this.startActivity(intent)
    }

    fun loadNavigation() {
        this.runOnUiThread {
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

        this.runOnUiThread {
            this.progressBarHolder.animation = inAnimation
            this.progressBarHolder.visibility = View.VISIBLE
        }
    }

    fun hideLoading() {
        val outAnimation = AlphaAnimation(1.0f, 0.0f)
        outAnimation.duration = 200

        this.runOnUiThread {
            this.progressBarHolder.animation = outAnimation
            this.progressBarHolder.visibility = View.GONE
        }
    }

    override fun setTitle(titleId: Int) {
        this.setTitle(this.getString(titleId))
    }

    fun setTitle(title: String? = null) {
        this.runOnUiThread {
            var newTitle = this.getAccount().alias

            if (title?.isNotEmpty() == true) {
                newTitle += " - $title"
            }

            this.findViewById<TextView>(android.R.id.title).text = title ?: newTitle
            super.setTitle(newTitle)
            this.setTaskDescription(ActivityManager.TaskDescription(newTitle))
        }
    }

    fun startActivity(module: String, task: String, action: String, id: Any, extras: Map<String, Any>) {
        val account = this.application.getAccountById(this.getAccount().id)
            ?: throw AccountException("Account " + this.getAccount().id + " not found in store!")

        val activity = this.application.getActivity(
            account,
            module,
            task,
            action,
            id
        )

        if (activity == null) {
            val intent = Intent(this, Class.forName(this.application.getActivityName(module, task, action)))
            intent.putExtra(ACCOUNT_KEY, account.account)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK

            extras.forEach {
                when (val value = it.value) {
                    is Parcelable -> intent.putExtra(it.key, value)
                    is Serializable -> intent.putExtra(it.key, value)
                    is String -> intent.putExtra(it.key, value)
                    is Int -> intent.putExtra(it.key, value)
                    is Long -> intent.putExtra(it.key, value)
                    is Float -> intent.putExtra(it.key, value)
                    is Double -> intent.putExtra(it.key, value)
                    is Boolean -> intent.putExtra(it.key, value)
                    else -> throw ActivityException(it.key + " cant put as extra. Type not allowed")
                }
            }

            this.startActivity(intent)

            return
        }

        val activityManager = activity.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(activity.taskId, 0)
    }

    fun runTask(run: () -> Unit, runFailure: ((exception: Throwable) -> Unit)? = null) {
        CompletableFuture.supplyAsync<Any> {
            try {
                run()
            } catch (exception: MessageException) {
                this.runOnUiThread {
                    var message = exception.message
                    val messageRessource = exception.messageRessource

                    if (messageRessource != null) {
                        message = getString(messageRessource)
                    }

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                    if (runFailure != null) {
                        runFailure(exception)
                    }
                }
            }
        }.exceptionally { e -> e.printStackTrace() }
    }

    override fun onResume() {
        super.onResume()
        val update = this.update

        if (update != null) {
            this.runTask({
                DeviceTask.addPush(this, update)
            })
        }
    }

    override fun onPause() {
        super.onPause()
        val update = this.update

        if (update != null) {
            this.runTask({
                DeviceTask.removePush(this, update)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Config.LOG_TAG, "Destroy activity")
        val account = this.application.getAccountById(this.getAccount().id) ?: return
        val process = account.getProcesses().find {
            it.activity == this
        } ?: return
        account.removeProccess(process)
    }

    companion object {
        const val ACCOUNT_KEY = "account"
        const val SHORTCUT_KEY = "shortcut"
    }
}