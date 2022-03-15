package de.wollis_page.gibsonos.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.dto.ListResponse
import de.wollis_page.gibsonos.helper.ListInterface

class BaseListAdapter(private val context: Activity, private val list: ListInterface) : RecyclerView.Adapter<BaseListAdapter.ItemHolder>() {
    private val inflater = LayoutInflater.from(context)
    var items: MutableList<ListItemInterface> = ArrayList()
    var total: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            this.list,
            this.inflater.inflate(this.list.getListRessource(), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(this.items[position])
    }

    override fun getItemCount() = this.items.size

    fun setListResponse(listResponse: ListResponse<*>) {
        this.items = listResponse.data.toMutableList() as MutableList<ListItemInterface>
        this.total = listResponse.total
    }

    class ItemHolder(
        private var context: ListInterface,
        private var view: View
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var item: ListItemInterface

        init {
            view.setOnClickListener(this)
        }

        fun bind(item: ListItemInterface) {
            this.item = item
            this.context.bind(item, this.view)
        }

        override fun onClick(p0: View?) {
            this.context.onClick(this.item)
        }
    }
}