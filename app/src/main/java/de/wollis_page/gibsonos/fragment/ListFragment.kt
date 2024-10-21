package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.helper.ListInterface

abstract class ListFragment : GibsonOsFragment(), ListInterface {
    override lateinit var listView: RecyclerView
    override lateinit var listAdapter: BaseListAdapter
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    open fun getLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(this.activity)

    override fun updateData(data: String) {
        val update = this.activity.update ?: return

        this.updateList(data, update.dtoClass.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(this.getContentView(), container, false)
        this.listView = view.findViewById(R.id.list)

        val llm = this.getLayoutManager() as LinearLayoutManager
        llm.orientation = LinearLayoutManager.VERTICAL
        this.listView.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(activity, llm.orientation)
        this.listView.addItemDecoration(dividerItemDecoration)

        this.listAdapter = BaseListAdapter(this.activity, this)
        this.listView.adapter = this.listAdapter

        this.loadList()

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
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

        return view
    }

    protected fun load(run: (account: Account) -> Unit) {
        val accountModel = this.activity.getAccount()

        this.activity.runTask({
            val account = this.activity.application.getAccountById(accountModel.id)

            if (account === null) {
                this.activity.runOnUiThread {
                    Toast.makeText(activity, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    this.activity.finish()
                }

                return@runTask
            }

            run(account)
            activity.runOnUiThread { this.listView.adapter?.notifyDataSetChanged() }
        })
    }

    fun runTask(run: () -> Unit, runFailure: ((exception: Throwable) -> Unit)? = null) {
        this.activity.runTask(run, runFailure)
    }

    fun getAccount(): de.wollis_page.gibsonos.model.Account {
        return this.activity.getAccount()
    }
}