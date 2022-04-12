package de.wollis_page.gibsonos.helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.activity.ListActivity

class ListItemTouchHelper(private val context: ListActivity): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        this.context.onSwiped(this.context.listAdapter.items.get(viewHolder.absoluteAdapterPosition), direction)
//        this.context.listAdapter.notifyItemRemoved(viewHolder.absoluteAdapterPosition)
        this.context.listAdapter.notifyDataSetChanged()
    }
}