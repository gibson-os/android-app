package de.wollis_page.gibsonos.activity

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.application.GibsonOsApplication
import de.wollis_page.gibsonos.dto.NavigationItem
import de.wollis_page.gibsonos.dto.Update
import de.wollis_page.gibsonos.exception.AccountException
import de.wollis_page.gibsonos.exception.MessageException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.task.DeviceTask
import de.wollis_page.gibsonos.service.AppIntentExtraService
import de.wollis_page.gibsonos.service.NavigationService
import java.util.concurrent.CompletableFuture

abstract class GibsonOsActivity : AppCompatActivity() {
    private var account: Account? = null
    private var shortcut: Shortcut? = null
    lateinit var application: GibsonOsApplication
    lateinit var contentContainer: ConstraintLayout
    private lateinit var navigationView: NavigationView
    private lateinit var progressBarHolder: FrameLayout
    protected lateinit var navigationService: NavigationService
    private var navigationItem: NavigationItem? = null
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

    fun getShortcut(): Shortcut? {
        return this.shortcut
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.setContentView(R.layout.base_layout)
        super.onCreate(savedInstanceState)
        this.application = this.getApplication() as GibsonOsApplication

        val accountModel = AppIntentExtraService.getIntentExtra(ACCOUNT_KEY, this.intent) as Account?
        val shortcutDto = AppIntentExtraService.getIntentExtra(SHORTCUT_KEY, this.intent) as Shortcut?

        this.account = accountModel
        this.shortcut = shortcutDto

        this.createContentContainer()
        this.createToolbar()

        if (accountModel !== null && shortcutDto !== null) {
            this.navigationItem = this.application.addNavigationItem(accountModel, shortcutDto)
        }

        this.navigationService = NavigationService(this)
        this.progressBarHolder = this.findViewById(R.id.progressBarHolder)
    }

    private fun createContentContainer() {
        this.contentContainer = this.findViewById(R.id.content) as ConstraintLayout
        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            this.getContentView(),
            this.findViewById(android.R.id.content),
            false
        ))
    }

    private fun createToolbar() {
        val toolbarLeft = this.findViewById<Toolbar>(R.id.toolbarLeft)
        val drawer = this.findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbarLeft,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val toolbar = this.findViewById<Toolbar>(R.id.toolbar)
        this.setSupportActionBar(toolbar);
        this.supportActionBar?.setDisplayShowTitleEnabled(false);
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
            super.setTitle(title ?: newTitle)
            this.setTaskDescription(ActivityManager.TaskDescription(newTitle))
            this.navigationItem?.shortcut?.text = title ?: newTitle ?: this.shortcut?.text ?: ""
            this.navigationService.loadNavigation()
        }
    }

    fun runTask(run: () -> Unit, runFailure: ((exception: Throwable) -> Unit)? = null) {
        CompletableFuture.supplyAsync<Any> {
            try {
                run()
            } catch (exception: MessageException) {
                this.runOnUiThread {
                    var message = exception.message
                    val messageRessource = exception.messageResource

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

    protected fun addSearch(view: ConstraintLayout = this.contentContainer) {
        val inflater = LayoutInflater.from(this)
        view.addView(inflater.inflate(
            R.layout.base_button_search,
            this.findViewById(android.R.id.content),
            false
        ))
        val searchButton = findViewById<FloatingActionButton>(R.id.searchButton)
        searchButton.setOnClickListener {
            val searchInput = findViewById<TextInputEditText>(R.id.searchText)
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            if (searchInput.visibility == View.VISIBLE) {
                this.hideSearch()

                return@setOnClickListener
            }

            searchInput.visibility = View.VISIBLE
            searchInput.requestFocus()
            inputMethodManager.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    protected fun hideSearch() {
        val searchInput = findViewById<TextInputEditText>(R.id.searchText)
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        searchInput.visibility = View.INVISIBLE
        searchInput.clearFocus()
        searchInput.text?.clear()
        inputMethodManager.hideSoftInputFromWindow(searchInput.windowToken, 0)
    }

    protected fun removeHeder() {
        val header = this.findViewById<AppBarLayout>(R.id.header)
        (header.parent as ViewManager).removeView(header)
    }

    override fun onResume() {
        super.onResume()
        val update = this.update

        if (update != null) {
            this.runTask({
                DeviceTask.addPush(this, update)
            })
        }

        this.navigationService.loadNavigation()
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

        if (this.account != null) {
            val account = this.application.getAccountById(this.getAccount().id) ?: return
            val navigationItemDto = this.navigationItem ?: return

            account.removeNavigationItem(navigationItemDto)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

//        this.navigationItem?.instanceState = outState
    }

    companion object {
        const val ACCOUNT_KEY = "account"
        const val SHORTCUT_KEY = "shortcut"
    }
}