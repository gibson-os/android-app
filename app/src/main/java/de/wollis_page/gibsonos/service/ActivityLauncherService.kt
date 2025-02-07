package de.wollis_page.gibsonos.service

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.exception.AccountException
import de.wollis_page.gibsonos.exception.ActivityException
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import java.io.Serializable

object ActivityLauncherService {
    fun startActivity(
        context: GibsonOsActivity,
        shortcut: Shortcut,
        extras: Map<String, Any>,
        launcher: ActivityResultLauncher<Intent>? = null,
        bundle: Bundle? = null,
    ) {
        val application = context.application
        val accountId = context.getAccount().id
        val account = application.getAccountById(accountId)
            ?: throw AccountException("Account $accountId not found in store!")

        this.startActivity(
            context,
            shortcut.module,
            shortcut.task,
            shortcut.action,
            extras,
            account,
            launcher,
            bundle
        )
    }

    fun startActivity(
        context: GibsonOsActivity,
        module: String,
        task: String,
        action: String,
        extras: Map<String, Any>,
        launcher: ActivityResultLauncher<Intent>? = null
    ) {
        val application = context.application
        val accountId = context.getAccount().id

        this.startActivity(
            context,
            module,
            task,
            action,
            extras,
            application.getAccountById(accountId)
                ?: throw AccountException("Account $accountId not found in store!"),
            launcher
        )
    }

    fun startActivity(
        context: GibsonOsActivity,
        module: String,
        task: String,
        action: String,
        extras: Map<String, Any>,
        account: Account,
        launcher: ActivityResultLauncher<Intent>? = null,
        bundle: Bundle? = null,
    ) {
        val shortcut = extras[GibsonOsActivity.SHORTCUT_KEY] as Shortcut?
        val intent = Intent(
            context,
            ActivityMatcher.getActivity(module, task, action, shortcut?.parameters),
        )
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        AppIntentExtraService.setIntentExtra(GibsonOsActivity.ACCOUNT_KEY, account.account, intent)

        extras.forEach {
            when (val value = it.value) {
                is Parcelable -> AppIntentExtraService.setIntentExtra(it.key, value, intent)
                is String -> intent.putExtra(it.key, value)
                is Int -> intent.putExtra(it.key, value)
                is Long -> intent.putExtra(it.key, value)
                is Float -> intent.putExtra(it.key, value)
                is Double -> intent.putExtra(it.key, value)
                is Boolean -> intent.putExtra(it.key, value)
                is Serializable -> intent.putExtra(it.key, value)
                else -> throw ActivityException(it.key + " cant put as extra. Type not allowed")
            }
        }

        if (launcher == null) {
            context.startActivity(intent, bundle)

            return
        }

        launcher.launch(intent)
    }
}