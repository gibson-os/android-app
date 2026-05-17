package de.wollis_page.gibsonos.helper

import android.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R

class ListItemTouchHelper(private val listInterface: ListInterface): ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val index = viewHolder.absoluteAdapterPosition
        val item = listInterface.listAdapter.items.get(index)
        val deleteTitle = listInterface.getDeleteTitle()
        val deleteMessage = listInterface.getDeleteMessage(item)

        if (deleteTitle == null) {
            return
        }

        val builder = AlertDialog.Builder(listInterface.activity)

        with (builder) {
            setTitle(deleteTitle)
            setPositiveButton(R.string.delete) { _, _ ->
                if (!listInterface.deleteItem(item)) {
                    listInterface.listAdapter.notifyItemChanged(index)

                    return@setPositiveButton
                }

                listInterface.listAdapter.items.removeAt(index)
                listInterface.listAdapter.notifyItemRemoved(index)
            }
            setNegativeButton(R.string.cancel) { _, _ ->
                listInterface.listAdapter.notifyItemChanged(index)
            }
        }

        if (deleteMessage != null) {
            builder.setMessage(deleteMessage)
        }

        builder.show()
    }
}
