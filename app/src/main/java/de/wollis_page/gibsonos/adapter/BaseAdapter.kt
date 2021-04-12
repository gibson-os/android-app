package de.wollis_page.gibsonos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface

class BaseAdapter(private val context: ListActivity) : RecyclerView.Adapter<BaseAdapter.ItemHolder>() {
    private val inflater = LayoutInflater.from(context)
    var items: MutableList<ListItemInterface> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            this.context,
            this.inflater.inflate(this.context.getListRessource(), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(this.items[position])
    }

    override fun getItemCount() = this.items.size

    class ItemHolder(
        private var context: ListActivity,
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
            this.context.onCLick(this.item)
        }
    }
}