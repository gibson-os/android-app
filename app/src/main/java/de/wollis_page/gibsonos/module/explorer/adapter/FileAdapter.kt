package de.wollis_page.gibsonos.module.explorer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.explorer.dto.Item
import kotlin.collections.ArrayList

class FileAdapter(context: Context, private val account: Account) : RecyclerView.Adapter<FileAdapter.ItemHolder>() {
    private val inflater = LayoutInflater.from(context)
    var items: MutableList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            this.inflater.inflate(R.layout.explorer_index_list_item, parent, false),
            this.account
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(this.items[position])
    }

    override fun getItemCount() = this.items.size

    class ItemHolder(private var view: View, private var account: Account) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var item: Item

        init {
            view.setOnClickListener(this)
        }

        fun bind(item: Item) {
            this.item = item

            (view.findViewById<View>(R.id.name) as TextView).text = item.name
            (view.findViewById<View>(R.id.path) as TextView).text = item.path
        }

        override fun onClick(p0: View?) {
            val context = this.itemView.context
            Toast.makeText(context, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
        }
    }
}