package de.wollis_page.gibsonos.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.ListInterface

abstract class ListActivity : GibsonOsActivity(), ListInterface {
    private lateinit var listView: RecyclerView
    protected lateinit var listAdapter: BaseListAdapter
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.listView = this.findViewById(R.id.list)

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
        }, {
            this.finish()
        })
    }

    protected fun addSearch() {
        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            R.layout.base_button_search,
            this.findViewById(android.R.id.content),
            false
        ))
        val searchButton = findViewById<FloatingActionButton>(R.id.searchButton)
        searchButton.setOnClickListener {
            Log.d(Config.LOG_TAG, "Click")
        }
    }
}