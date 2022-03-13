package de.wollis_page.gibsonos.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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

abstract class ListFragment : Fragment(), ListInterface {
    private lateinit var listView: RecyclerView
    protected lateinit var listAdapter: BaseListAdapter
    protected lateinit var arguments: Map<String, *>

    override fun getGibsonOsActivity(): Activity {
        return this.requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(this.getContentView(), container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.arguments = requireArguments().getParcelable("arguments")!!

        val activity = this.getGibsonOsActivity()
        this.listView = activity.findViewById(R.id.list)

        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        this.listView.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(activity, llm.orientation)
        this.listView.addItemDecoration(dividerItemDecoration)

        this.listAdapter = BaseListAdapter(this)
        this.listView.adapter = this.listAdapter

        this.loadList()

        val swipeContainer = activity.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            this.loadList()
            swipeContainer.isRefreshing = false
        }
    }

    protected fun load(run: (account: Account) -> Unit) {
        val activity = this.getGibsonOsActivity()
        val accountModel = activity.getAccount()

        this.runTask({
            val account = activity.application.getAccountById(accountModel.id)

            if (account === null) {
                activity.runOnUiThread {
                    Toast.makeText(this, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    this.finish()
                }

                return@runTask
            }

            run(account)
            activity.runOnUiThread { this.listView.adapter?.notifyDataSetChanged() }
        }, {
            activity.finish()
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