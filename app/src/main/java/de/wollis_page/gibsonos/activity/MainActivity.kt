package de.wollis_page.gibsonos.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.ListActivity
import de.wollis_page.gibsonos.dto.ListInterface
import de.wollis_page.gibsonos.model.Account

class MainActivity : ListActivity() {
    override fun getListRessource() = R.layout.account_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            R.layout.base_add_button,
            this.findViewById(android.R.id.content),
            false
        ))
        val addButton = findViewById<FloatingActionButton>(R.id.addButton)
        addButton.setOnClickListener {
            startActivityForResult(
                Intent(applicationContext, LoginActivity::class.java),
                100
            )
        }

        loadList()
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

        this.adapter.items = accounts.toMutableList()
        this.adapter.notifyDataSetChanged()
    }

    override fun onCLick(item: ListInterface) {
        if (item !is Account) {
            return
        }

        val intent = Intent(this, DesktopActivity::class.java)
        intent.putExtra(ACCOUNT_KEY, item)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        this.startActivity(intent)
    }

    override fun bind(item: ListInterface, view: View) {
        if (item !is Account) {
            return
        }

        (view.findViewById<View>(R.id.alias) as TextView).text = item.alias
        (view.findViewById<View>(R.id.url) as TextView).text = item.url
        (view.findViewById<View>(R.id.user) as TextView).text = item.user
    }
}