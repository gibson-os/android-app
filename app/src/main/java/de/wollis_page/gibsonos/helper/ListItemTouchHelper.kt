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
        val index = viewHolder.absoluteAdapterPosition

        if (!this.context.deleteItem(this.context.listAdapter.items.get(index))) {
            this.context.listAdapter.notifyItemChanged(index)

            return
        }

        this.context.listAdapter.items.removeAt(index)
        this.context.listAdapter.notifyItemRemoved(index)
    }
}