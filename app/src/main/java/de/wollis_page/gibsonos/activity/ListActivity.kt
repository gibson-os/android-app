package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.helper.ListInterface
import de.wollis_page.gibsonos.helper.ListItemTouchHelper

abstract class ListActivity : GibsonOsActivity(), ListInterface {
    override lateinit var listView: RecyclerView
    override lateinit var listAdapter: BaseListAdapter
    override var activity: GibsonOsActivity = this
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.listView = this.findViewById(R.id.list)

        if (this.getDeleteTitle() != null) {
            ItemTouchHelper(ListItemTouchHelper(this)).attachToRecyclerView(this.listView)
        }

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        this.listView.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(this, llm.orientation)
        this.listView.addItemDecoration(dividerItemDecoration)

        this.listAdapter = BaseListAdapter(this, this)
        this.listView.adapter = this.listAdapter

        this.loadList()

        val swipeContainer = this.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            this.loadList()
            swipeContainer.isRefreshing = false
        }

        this.scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager?.itemCount ?: 0

                if (
                    totalItemCount == llm.findLastVisibleItemPosition() + 1 &&
                    totalItemCount < listAdapter.total
                ) {
                    loadList(totalItemCount.toLong())
                }
            }
        }
        this.listView.addOnScrollListener(this.scrollListener)
    }

    override fun updateData(data: String) {
        val update = this.update ?: return

        this.updateList(data, update.dtoClass.java)
    }

    protected fun load(run: (account: Account) -> Unit) {
        val accountModel = this.getAccount()

        this.runTask({
            val account = this.application.getAccountById(accountModel.id)

            if (account === null) {
                this.runOnUiThread {
                    Toast.makeText(this, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    this.finish()
                }

                return@runTask
            }

            run(account)
            this.runOnUiThread { this.listView.adapter?.notifyDataSetChanged() }
        })
    }

    protected fun addSearch(filter: (item: ListItemInterface, searchTerm: String) -> Boolean) {
        this.listAdapter.listFilter = filter
        this.addSearch()

        val searchInput = findViewById<TextInputEditText>(R.id.searchText)
        searchInput.addTextChangedListener {
            this.listAdapter.filter.filter(it)
        }
    }
}