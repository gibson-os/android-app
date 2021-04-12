package de.wollis_page.gibsonos.activity.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.dto.ListInterface
import de.wollis_page.gibsonos.exception.MessageException
import java.util.concurrent.CompletableFuture

abstract class ListActivity : GibsonOsActivity() {
    protected lateinit var listView: RecyclerView
    protected lateinit var adapter: BaseAdapter

    abstract fun onCLick(item: ListInterface)

    abstract fun bind(item: ListInterface, view: View)

    abstract fun getListRessource(): Int

    override fun getContentView() = R.layout.base_list

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        this.listView = findViewById(R.id.list)
        this.listView.layoutManager = LinearLayoutManager(this)

        this.adapter = BaseAdapter(this)
        this.listView.adapter = this.adapter

        super.onCreate(savedInstanceState)
    }

    protected fun load(run: (account: Account) -> Unit) {
        val me = this
        val accountModel = this.getAccount()

        CompletableFuture.supplyAsync<Any> {
            val account = me.application.getAccountById(accountModel.id)

            if (account === null) {
                me.runOnUiThread {
                    Toast.makeText(me, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    me.finish()
                }

                return@supplyAsync
            }

            try {
                run(account)
                Handler(Looper.getMainLooper()).post { me.listView.adapter?.notifyDataSetChanged() }
            } catch (exception: MessageException) {
                me.runOnUiThread {
                    var message = exception.message
                    val messageRessource = exception.messageRessource

                    if (messageRessource != null) {
                        message = getString(messageRessource)
                    }

                    Toast.makeText(me, message, Toast.LENGTH_LONG).show()
                    me.finish()
                }
            }
        }.exceptionally { e -> e.printStackTrace() }
    }
}