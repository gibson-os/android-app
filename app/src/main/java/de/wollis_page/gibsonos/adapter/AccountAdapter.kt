package de.wollis_page.gibsonos.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.DesktopActivity
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.model.Account

class AccountAdapter(context: Context) : RecyclerView.Adapter<AccountAdapter.AccountHolder>() {
    private val inflater = LayoutInflater.from(context)
    var accounts: MutableList<Account> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
        return AccountHolder(this.inflater.inflate(R.layout.account_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: AccountHolder, position: Int) {
        holder.bind(this.accounts[position])
    }

    override fun getItemCount() = this.accounts.size

    class AccountHolder(private var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var account: Account? = null

        init {
            view.setOnClickListener(this)
        }

        fun bind(account: Account) {
            this.account = account

            (view.findViewById<View>(R.id.alias) as TextView).text = account.alias
            (view.findViewById<View>(R.id.url) as TextView).text = account.url
            (view.findViewById<View>(R.id.user) as TextView).text = account.user
        }

        override fun onClick(p0: View?) {
            val context = this.itemView.context
            val intent = Intent(context, DesktopActivity::class.java)
            intent.putExtra(GibsonOsActivity.ACCOUNT_KEY, this.account)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }
}