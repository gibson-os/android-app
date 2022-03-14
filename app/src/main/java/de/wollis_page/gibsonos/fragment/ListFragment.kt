package de.wollis_page.gibsonos.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.dto.Account
import de.wollis_page.gibsonos.exception.ActivityException
import de.wollis_page.gibsonos.helper.ListInterface

abstract class ListFragment : Fragment(), ListInterface {
    private lateinit var listView: RecyclerView
    protected lateinit var listAdapter: BaseListAdapter
    protected lateinit var fragmentsArguments: HashMap<String, *>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(this.getContentView(), container, false)
        val activity = this.requireActivity()
        this.listView = view.findViewById(R.id.list)

        val llm = LinearLayoutManager(activity)
        llm.orientation = LinearLayoutManager.VERTICAL
        this.listView.layoutManager = llm
        val dividerItemDecoration = DividerItemDecoration(activity, llm.orientation)
        this.listView.addItemDecoration(dividerItemDecoration)

        this.listAdapter = BaseListAdapter(this.requireActivity(), this)
        this.listView.adapter = this.listAdapter

        this.loadList()

        val swipeContainer = view.findViewById<SwipeRefreshLayout>(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            this.loadList()
            swipeContainer.isRefreshing = false
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.takeIf { it.containsKey("arguments") }?.apply {
            fragmentsArguments = getSerializable("arguments") as HashMap<String, *>
        }
    }

    protected fun load(run: (account: Account) -> Unit) {
        val activity = this.getGibsonOsActivity()
        val accountModel = activity.getAccount()

        activity.runTask({
            val account = activity.application.getAccountById(accountModel.id)

            if (account === null) {
                activity.runOnUiThread {
                    Toast.makeText(activity, R.string.account_error_no_model_found, Toast.LENGTH_LONG).show()
                    activity.finish()
                }

                return@runTask
            }

            run(account)
            activity.runOnUiThread { this.listView.adapter?.notifyDataSetChanged() }
        }, {
            activity.finish()
        })
    }

    fun runTask(run: () -> Unit, runFailure: ((exception: Throwable) -> Unit)? = null) {
        this.getGibsonOsActivity().runTask(run, runFailure)
    }

    fun getAccount(): de.wollis_page.gibsonos.model.Account {
        return this.getGibsonOsActivity().getAccount()
    }

    protected fun getGibsonOsActivity(): GibsonOsActivity {
        val activity = this.requireActivity()

        if (activity !is GibsonOsActivity) {
            throw ActivityException("Activity is no instance of GibsonOsActivity!")
        }

        return activity
    }
}