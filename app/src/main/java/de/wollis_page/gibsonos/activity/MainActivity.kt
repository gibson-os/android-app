package de.wollis_page.gibsonos.activity

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.AppCompatListActivity
import de.wollis_page.gibsonos.adapter.AccountAdapter

class MainActivity : AppCompatListActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)
        navigationView?.setNavigationItemSelectedListener(this)
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
        val accounts = application?.accountModels

        if (accounts === null) {
            return
        }

        if (accounts.size == 0) {
            this.startActivityForResult(Intent(this.applicationContext, LoginActivity::class.java), 100)
        }

        val accountAdapter = AccountAdapter(this.applicationContext, accounts)
        setListAdapter(accountAdapter)
        this.listView?.onItemClickListener = OnItemClickListener {
            adapterView, view, i, l -> runActivity(DesktopActivity::class.java, accounts[i])
        }
    }
}