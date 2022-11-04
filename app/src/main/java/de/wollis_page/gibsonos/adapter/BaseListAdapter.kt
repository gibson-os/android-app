package de.wollis_page.gibsonos.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.helper.ListInterface

class BaseListAdapter(
    private val context: Activity,
    private val list: ListInterface,
): RecyclerView.Adapter<BaseListAdapter.ItemHolder>(), Filterable {
    private val inflater = LayoutInflater.from(context)
    var listFilter: ((item: ListItemInterface, searchTerm: String) -> Boolean)? = null
    var items = ArrayList<ListItemInterface>()
        set(value) {
            field = value
            this.filteredItems = value
        }
    var filteredItems = ArrayList<ListItemInterface>()
    var total: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            this.list,
            this.inflater.inflate(this.list.getListRessource(), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(this.filteredItems[position])
    }

    override fun getItemCount() = this.filteredItems.size

    fun setListResponse(listResponse: ListResponse<*>) {
        this.total = listResponse.total

        if (listResponse.start > 0) {
            this.items.addAll(listResponse.data.toMutableList() as ArrayList<ListItemInterface>)
            this.filteredItems.addAll(listResponse.data.toMutableList() as ArrayList<ListItemInterface>)

            return
        }

        this.items = listResponse.data.toMutableList() as ArrayList<ListItemInterface>
        this.filteredItems = listResponse.data.toMutableList() as ArrayList<ListItemInterface>
    }

    class ItemHolder(
        private var context: ListInterface,
        private var view: View
    ) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        private lateinit var item: ListItemInterface

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        fun bind(item: ListItemInterface) {
            this.item = item
            this.context.bind(item, this.view)
        }

        override fun onClick(p0: View?) {
            this.context.onClick(this.item)
        }

        override fun onLongClick(p0: View?): Boolean {
            return this.context.onLongClick(this.item)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                var filteredResult = ArrayList<ListItemInterface>()

                if (charString.isEmpty() || listFilter == null) {
                    filteredResult = items
                } else {
                    items.filter {
                        listFilter!!.invoke(it, charString)
                    }.forEach {
                        filteredResult.add(it)
                    }
                }

                return FilterResults().apply { values = filteredResult }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d(Config.LOG_TAG, results?.values.toString())
                filteredItems = if (results?.values == null) {
                    ArrayList()
                } else {
                    results.values as ArrayList<ListItemInterface>
                }

                notifyDataSetChanged()
            }
        }
    }
}