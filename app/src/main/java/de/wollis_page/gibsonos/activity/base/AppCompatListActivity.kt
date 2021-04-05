package de.wollis_page.gibsonos.activity.base

import android.widget.ListAdapter
import android.widget.ListView

abstract class AppCompatListActivity : GibsonOsActivity() {
    protected var listView: ListView? = null
        get() {
            if (field == null) {
                field = findViewById(android.R.id.list)
            }
            return field
        }
        private set

    protected fun setListAdapter(adapter: ListAdapter?) {
        listView!!.adapter = adapter
    }
}