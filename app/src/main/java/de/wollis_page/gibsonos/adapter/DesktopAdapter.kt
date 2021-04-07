package de.wollis_page.gibsonos.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.desktop.Item
import de.wollis_page.gibsonos.helper.Config

class DesktopAdapter(context: Context, private val desktop: MutableList<Item>) : RecyclerView.Adapter<DesktopAdapter.ItemHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(this.inflater.inflate(R.layout.desktop_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(this.desktop[position])
    }

    override fun getItemCount() = this.desktop.size

    class ItemHolder(private var view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private var item: Item? = null

        init {
            view.setOnClickListener(this)
        }

        fun bind(item: Item) {
            this.item = item

            (view.findViewById<View>(R.id.text) as TextView).text = item.text
            (view.findViewById<View>(R.id.module) as TextView).text = item.module
        }

        override fun onClick(p0: View?) {
            Log.d(Config.LOG_TAG, "Click!")
        }
    }
}