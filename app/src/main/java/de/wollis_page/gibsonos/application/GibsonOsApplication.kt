package de.wollis_page.gibsonos.application

import android.util.Log
import com.orm.SugarApp
import com.orm.SugarRecord
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.process.Process
import java.util.*
import de.wollis_page.gibsonos.model.Account as AccountModel

class GibsonOsApplication : SugarApp() {
    private val accounts: MutableList<Account> = ArrayList()

    override fun onCreate() {
        super.onCreate()

        for (account in SugarRecord.listAll<AccountModel>(AccountModel::class.java)) {
            Log.d(Config.LOG_TAG, "onCreate: " + account.id)
            addAccount(account)
        }
    }

    fun addAccount(account: AccountModel) {
        accounts.add(Account(account))
    }

    fun getAccounts(): List<Account> {
        return accounts
    }

    fun addProcess(activity: GibsonOsActivity) {
        val account = activity.account
        val accountDto = getAccount(account!!)!!

        accountDto.addProccess(Process(
                activity.title.toString(),
                account,
                activity.javaClass,
                activity.intent.extras!!
        ))
    }

    private fun getAccount(account: AccountModel): Account? {
        for (accountDto in accounts) {
            if (accountDto.account.id == account.id) {
                return accountDto
            }
        }

        return null
    }

    val accountModels: List<AccountModel>
        get() {
            val accounts: MutableList<AccountModel> = ArrayList()

            for (accountDto in this.accounts) {
                accounts.add(accountDto.account)
            }

            return accounts
        }
}