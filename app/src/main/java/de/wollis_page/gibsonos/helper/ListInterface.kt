package de.wollis_page.gibsonos.helper

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.adapter.BaseListAdapter
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException

interface ListInterface {
    var listView: RecyclerView
    var listAdapter: BaseListAdapter
    var activity: GibsonOsActivity

    fun onClick(item: ListItemInterface)

    fun bind(item: ListItemInterface, view: View)

    fun getListRessource(): Int

    fun loadList(start: Long = 0, limit: Long = 100)

    fun getContentView() = R.layout.base_list

    fun onLongClick(item: ListItemInterface): Boolean {
        return false
    }

    fun deleteItem(item: ListItemInterface): Boolean {
        return false
    }

    fun getDeleteTitle(): String? {
        return null
    }

    fun getDeleteMessage(item: ListItemInterface): String? {
        return null
    }

    fun updateList(data: String, dtoClass: Class<*>) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val listType = Types.newParameterizedType(MutableList::class.java, dtoClass)
        val jsonAdapter = moshi.adapter<MutableList<ListItemInterface>>(listType)
        val list = jsonAdapter.fromJson(data) ?:
        throw AppException("Update list failed!")
        val newList = mutableListOf<ListItemInterface>()
        Log.d(Config.LOG_TAG, list.toString())
        this.listAdapter.items.forEach {
            try {
                val item = it
                newList.add(list.first { it.getId() == item.getId() })
                Log.d(Config.LOG_TAG, "new")
            } catch (exception: NoSuchElementException) {
                Log.d(Config.LOG_TAG, "old")
                newList.add(it)
            }
        }
        Log.d(Config.LOG_TAG, newList.toString())
        this.listAdapter.items = newList
        this.activity.runOnUiThread { this.listAdapter.notifyDataSetChanged() }
    }
}