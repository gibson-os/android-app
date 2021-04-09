package de.wollis_page.gibsonos.module.explorer.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.module.explorer.adapter.FileAdapter
import de.wollis_page.gibsonos.module.explorer.task.DirTask

class IndexActivity: ListActivity() {
    private lateinit var adapter: FileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.explorer_index_activity)
        super.onCreate(savedInstanceState)

        this.adapter = FileAdapter(this.applicationContext, this.getAccount())
        this.listView.adapter = this.adapter

        this.loadList()
    }

    private fun loadList() = this.load {
        val dir = DirTask.read(this, it.account)

        this.adapter.items = dir.data
    }
}