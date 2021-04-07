package de.wollis_page.gibsonos.activity.base

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R

abstract class ListActivity : GibsonOsActivity() {
    protected lateinit var listView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        this.listView = findViewById(android.R.id.list)
        this.listView.layoutManager = LinearLayoutManager(this)
        super.onCreate(savedInstanceState)
    }
}