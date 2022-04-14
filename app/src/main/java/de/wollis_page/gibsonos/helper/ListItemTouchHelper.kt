package de.wollis_page.gibsonos.helper

import android.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
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
        val activity = this.context
        val index = viewHolder.absoluteAdapterPosition
        val item = activity.listAdapter.items.get(index)
        val deleteTitle = activity.getDeleteTitle()
        val deleteMessage = activity.getDeleteMessage(item)

        if (deleteTitle == null) {
            return
        }

        val builder = AlertDialog.Builder(this.context)

        with (builder) {
            setTitle(deleteTitle)
            setPositiveButton(R.string.delete) { _, _ ->
                if (!activity.deleteItem(item)) {
                    activity.listAdapter.notifyItemChanged(index)

                    return@setPositiveButton
                }

                activity.listAdapter.items.removeAt(index)
                activity.listAdapter.notifyItemRemoved(index)
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                activity.listAdapter.notifyItemChanged(index)
            }
        }

        if (deleteMessage != null) {
            builder.setMessage(deleteMessage)
        }

        builder.show()
    }
}