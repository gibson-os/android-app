package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.MessageException
import java.util.concurrent.CompletableFuture

abstract class ListActivity : GibsonOsActivity() {
    protected lateinit var listView: RecyclerView
    protected lateinit var listAdapter: BaseListAdapter

    abstract fun onCLick(item: ListItemInterface)

    abstract fun bind(item: ListItemInterface, view: View)

    abstract fun getListRessource(): Int

    override fun getContentView() = R.layout.base_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.listView = this.findViewById(R.id.list)
        this.listView.layoutManager = LinearLayoutManager(this)

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        this.listView.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(this, llm.orientation)
        this.listView.addItemDecoration(dividerItemDecoration)

        this.listAdapter = BaseListAdapter(this)
        this.listView.adapter = this.listAdapter
    }

    protected fun load(run: (account: Account) -> Unit) {
        val accountModel = this.getAccount()

        CompletableFuture.supplyAsync<Any> {
            val account = this.application.getAccountById(accountModel.id)

            if (account === null) {
                this.runOnUiThread {
                    Toast.makeText(this, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    this.finish()
                }

                return@supplyAsync
            }

            try {
                run(account)
                this.runOnUiThread { this.listView.adapter?.notifyDataSetChanged() }
            } catch (exception: MessageException) {
                this.runOnUiThread {
                    var message = exception.message
                    val messageRessource = exception.messageRessource

                    if (messageRessource != null) {
                        message = getString(messageRessource)
                    }

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        }.exceptionally { e -> e.printStackTrace() }
    }
}