package de.wollis_page.gibsonos.application

import android.app.UiModeManager
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.orm.SugarApp
import com.orm.SugarRecord
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.exception.AccountException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.process.Process
import de.wollis_page.gibsonos.service.AppIntentExtraService
import de.wollis_page.gibsonos.model.Account as AccountModel


class GibsonOsApplication : SugarApp() {
    val accounts: MutableList<Account> = ArrayList()
    var firebaseToken: String? = null

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//        AppCompatDelegate.setDefaultNightMode(
//            if (this.useDarkMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
//        )

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(Config.LOG_TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            this.firebaseToken = task.result.toString()
            Log.d(Config.LOG_TAG, "Firebase token: " + this.firebaseToken)
        })

        for (account in SugarRecord.listAll(AccountModel::class.java)) {
            Log.d(Config.LOG_TAG, "onCreate: " + account.id)
            addAccount(account)
        }
    }

    fun addAccount(account: AccountModel) {
        this.accounts.add(Account(account))
    }

    fun removeAccount(account: AccountModel) {
        this.accounts.remove(this.getAccountById(account.id))
        SugarRecord.delete(account)
    }

    fun getAccountById(id: Long): Account? {
        return this.accounts.find { it.account.id == id }
    }

    fun useDarkMode(): Boolean {
        val uiModeManager = getSystemService(UI_MODE_SERVICE) as UiModeManager
        val sp = getSharedPreferences("settings", 0)

        return sp.getBoolean("darkMode", uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES)
    }

    fun setDarkMode(darkMode: Boolean) {
        val editor = getSharedPreferences("settings", 0).edit()
        editor.putBoolean("darkMode", darkMode)
        editor.apply()
    }

    fun addProcess(activity: GibsonOsActivity) {
        val account = activity.getAccount()
        val accountDto = this.getAccount(account)
            ?: throw AccountException("Account " + account.id + " not found in store!")

        accountDto.addProccess(Process(account, activity))
    }

    fun getAccountByToken(token: String): Account? {
        for (accountDto in accounts) {
            if (accountDto.account.token == token) {
                return accountDto
            }
        }

        return null
    }

    fun getActivity(
        account: Account,
        module: String,
        task: String,
        action: String,
        id: Any
    ): GibsonOsActivity? {
        val activityName = this.getActivityName(module, task, action)

        val process = account.getProcesses().find {
            it.activity::class.java.toString() == "class $activityName" && it.activity.getId() == id
        }

        return process?.activity
    }

    fun getActivity(
        account: Account,
        shortcut: Shortcut
    ): GibsonOsActivity? {
        val activityName = this.getActivityName(shortcut.module, shortcut.task, shortcut.action)

        val process = account.getProcesses().find {
            it.activity::class.java.toString() == "class $activityName" &&
            it.activity.isActivityforShotcut(shortcut)
        }

        return process?.activity
    }

    fun getActivityIntent(
        accountModel: AccountModel,
        module: String,
        task: String,
        action: String,
        id: Any
    ): Intent {
        val account = this.getAccountById(accountModel.id)
            ?: throw AccountException("Account " + accountModel.id + " not found in store!")

        val activity = this.getActivity(account, module, task, action, id)

        if (activity == null) {
            val intent = Intent(this, Class.forName(this.getActivityName(module, task, action)))
            AppIntentExtraService.setIntentExtra(GibsonOsActivity.ACCOUNT_KEY, accountModel, intent)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK

            return intent
        }

        return activity.intent
    }

    fun getActivityName(module: String, task: String, action: String): String
    {
        return "de.wollis_page.gibsonos.module." +
            module + "." +
            task + ".activity." +
            (action.replaceFirstChar { it.uppercase() }) + "Activity"
    }

    private fun getAccount(account: AccountModel): Account? {
        for (accountDto in accounts) {
            if (accountDto.account.id == account.id) {
                return accountDto
            }
        }

        return null
    }

    val accountModels: ArrayList<AccountModel>
        get() {
            val accounts: ArrayList<AccountModel> = ArrayList()

            for (accountDto in this.accounts) {
                accounts.add(accountDto.account)
            }

            return accounts
        }
}