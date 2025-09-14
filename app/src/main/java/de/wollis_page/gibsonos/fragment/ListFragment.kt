package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.callback.MenuVisibilityCallback
import de.wollis_page.gibsonos.dialog.FilterDialog
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.ListInterface

abstract class ListFragment : GibsonOsFragment(), ListInterface, MenuVisibilityCallback {
    override lateinit var listView: RecyclerView
    override lateinit var listAdapter: BaseListAdapter
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    override var selectedFilters: MutableMap<String, MutableList<String>> = mutableMapOf()

    override fun getContentView() = R.layout.base_list

    override fun getMenuView() = R.menu.base_list_menu

    open fun getLayoutManager(): RecyclerView.LayoutManager = LinearLayoutManager(this.activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (this.fragmentsArguments["selectedFilters"] is MutableMap<*, *>) {
            this.selectedFilters = this.fragmentsArguments["selectedFilters"] as MutableMap<String, MutableList<String>>
        }
    }

    override fun updateData(data: String) {
        val update = this.activity.update ?: return

        this.updateList(data, update.dtoClass.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        this.listView = view.findViewById(R.id.list)

        val llm = this.getLayoutManager() as LinearLayoutManager
        llm.orientation = LinearLayoutManager.VERTICAL
        this.listView.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(activity, llm.orientation)
        this.listView.addItemDecoration(dividerItemDecoration)

        this.listAdapter = BaseListAdapter(
            this.activity,
            this,
            this,
        )
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
            this.activity.runOnUiThread { this.listView.adapter?.notifyDataSetChanged() }
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.filter_menu_item -> {
                FilterDialog(
                    this.activity,
                    { filters ->
                        this.selectedFilters = filters
                        this.loadList()
                    }
                ).build(this.listAdapter.filters!!).show()

                true
            }
            R.id.sort_menu_item -> {
                true
            }
            else -> false
        }
    }

    fun runTask(run: () -> Unit, runFailure: ((exception: Throwable) -> Unit)? = null) {
        this.activity.runTask(run, runFailure)
    }

    fun getAccount(): de.wollis_page.gibsonos.model.Account {
        return this.activity.getAccount()
    }

    override fun updateFilterVisibility(visible: Boolean) {
        Log.d(Config.LOG_TAG, "updateFilterVisibility: $visible")
        this.activity.runOnUiThread {
            this.menu?.findItem(R.id.filter_menu_item)?.isVisible = visible
        }
    }

    override fun updateSortVisibility(visible: Boolean) {
        this.activity.runOnUiThread {
            this.menu?.findItem(R.id.sort_menu_item)?.isVisible = visible
        }
    }

//    override fun onResume() {
//        super.onResume()
//    }

    override fun onPrepareMenu(menu: Menu) {
        this.updateFilterVisibility((this.listAdapter.filters?.size ?: 0) > 0)
        this.updateSortVisibility((this.listAdapter.possibleOrders?.size ?: 0) > 0)
    }
}