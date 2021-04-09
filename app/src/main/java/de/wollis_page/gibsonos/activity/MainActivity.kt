package de.wollis_page.gibsonos.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.adapter.AccountAdapter

class MainActivity : ListActivity() {
    private lateinit var adapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        this.adapter = AccountAdapter(this.applicationContext)
        this.listView.adapter = this.adapter

        loadList()

        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener { startActivityForResult(Intent(applicationContext, LoginActivity::class.java), 100) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            loadList()
        }
    }

    private fun loadList() {
        val accounts = this.application.accountModels

        if (accounts.isEmpty()) {
            this.startActivityForResult(Intent(this.applicationContext, LoginActivity::class.java), 100)
        }

        this.adapter.accounts = accounts
        this.adapter.notifyDataSetChanged()
    }
}