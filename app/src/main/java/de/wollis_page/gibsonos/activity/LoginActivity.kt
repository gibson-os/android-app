package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.orm.SugarRecord
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.task.UserTask.login
import java.util.*
import java.util.concurrent.CompletableFuture

class LoginActivity : GibsonOsActivity() {
    private var editTexts: MutableList<EditText?>? = null
    private var etAlias: EditText? = null
    private var etUser: EditText? = null
    private var etPassword: EditText? = null
    private var etUrl: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)
        val me = this

        me.etAlias = me.findViewById<EditText>(R.id.alias)
        me.etUser = me.findViewById<EditText>(R.id.user)
        me.etPassword = me.findViewById<EditText>(R.id.password)
        me.etUrl = me.findViewById<EditText>(R.id.url)

        me.editTexts = ArrayList()
        me.editTexts?.add(me.etAlias)
        me.editTexts?.add(me.etUser)
        me.editTexts?.add(me.etPassword)
        me.editTexts?.add(etUrl)

        me.etAlias?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val accounts: List<Account> = SugarRecord.find<Account>(Account::class.java, "alias = ?", s.toString())

                if (accounts.size > 0) {
                    me.etAlias?.setError(getString(R.string.account_error_alias_exists))
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        val btnLogin = findViewById<Button>(R.id.login)

        btnLogin.setOnClickListener(View.OnClickListener {
            Log.i(Config.LOG_TAG, "Click")
            var hasErrors = false

            for (editText in me.editTexts as ArrayList<EditText?>) {
                if (editText?.text.toString().isEmpty()) {
                    editText?.error = getString(R.string.account_error_value_empty)
                }

                if (editText?.error.toString().isNotEmpty()) {
                    hasErrors = true
                }
            }

            if (hasErrors) {
                return@OnClickListener
            }

            CompletableFuture.supplyAsync<Any> { //v.setEnabled(false);
                val account = login(
                        me,
                        me.etUrl?.getText().toString(),
                        me.etUser?.getText().toString(),
                        me.etPassword?.getText().toString()
                )!!
                account.alias = me.etAlias?.getText().toString()
                account.url = me.etUrl?.getText().toString()
                account.save()
                me.application?.addAccount(account)

                //v.setEnabled(true);
                setResult(RESULT_OK)
                me.finish()
            }
        })
    }
}