package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.orm.SugarRecord
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.exception.MessageException
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.task.UserTask
import java.util.*
import java.util.concurrent.CompletableFuture

class LoginActivity : GibsonOsActivity() {
    private var editTexts: MutableList<EditText?> = ArrayList()
    private var etAlias: EditText? = null
    private var etUser: EditText? = null
    private var etPassword: EditText? = null
    private var etUrl: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)
        val me = this

        me.etAlias = me.findViewById(R.id.alias) as EditText
        me.etUser = me.findViewById(R.id.user) as EditText
        me.etPassword = me.findViewById(R.id.password) as EditText
        me.etUrl = me.findViewById(R.id.url) as EditText

        me.editTexts = ArrayList()
        me.editTexts.add(me.etAlias)
        me.editTexts.add(me.etUser)
        me.editTexts.add(me.etPassword)
        me.editTexts.add(etUrl)

        me.etAlias?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val accounts: List<Account> = SugarRecord.find(Account::class.java, "alias = ?", s.toString())

                if (accounts.isNotEmpty()) {
                    me.etAlias?.error = getString(R.string.account_error_alias_exists)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val btnLogin = findViewById<Button>(R.id.login)

        btnLogin.setOnClickListener OnClickListener@{
            var hasErrors = false

            for (editText in me.editTexts as ArrayList<EditText?>) {
                if (editText?.text.isNullOrBlank()) {
                    editText?.error = getString(R.string.account_error_value_empty)
                }

                if (!editText?.error.isNullOrEmpty()) {
                    hasErrors = true
                }
            }

            if (hasErrors) {
                Log.d(Config.LOG_TAG, "Login has errors")

                return@OnClickListener
            }

            CompletableFuture.supplyAsync<Any> {
                try {
                    val account = UserTask.login(
                        me,
                        me.etUrl?.text.toString(),
                        me.etUser?.text.toString(),
                        me.etPassword?.text.toString()
                    )
                    account.alias = me.etAlias?.text.toString()
                    account.save()
                    me.application.addAccount(account)

                    setResult(RESULT_OK)
                    me.finish()
                } catch (exception: MessageException) {
                    me.runOnUiThread {
                        var message = exception.message
                        val messageRessource = exception.messageRessource

                        if (messageRessource != null) {
                            message = getString(messageRessource)
                        }

                        Toast.makeText(me, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }.exceptionally { e -> e.printStackTrace() }
        }
    }
}