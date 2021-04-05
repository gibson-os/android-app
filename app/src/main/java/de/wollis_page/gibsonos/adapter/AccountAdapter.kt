package de.wollis_page.gibsonos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.model.Account

class AccountAdapter(context: Context?, accounts: List<Account>) : BaseAdapter() {
    private val accounts: List<Account>
    private val inflater: LayoutInflater

    override fun getCount(): Int {
        return accounts.size
    }

    override fun getItem(position: Int): Any {
        return accounts[position]
    }

    override fun getItemId(position: Int): Long {
        return accounts[position].id
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val view = inflater.inflate(R.layout.account_list_item, parent, false)
        (view.findViewById<View>(R.id.alias) as TextView).text = accounts[position].alias
        (view.findViewById<View>(R.id.url) as TextView).text = accounts[position].url
        (view.findViewById<View>(R.id.user) as TextView).text = accounts[position].user
        return view
    }

    init {
        inflater = LayoutInflater.from(context)
        this.accounts = accounts
    }
}