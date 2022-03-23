package de.wollis_page.gibsonos.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.module.core.desktop.activity.IndexActivity
import de.wollis_page.gibsonos.module.core.user.activity.LoginActivity

class MainActivity : ListActivity() {
    override fun getListRessource() = R.layout.base_account_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            R.layout.base_button_add,
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

    override fun loadList(start: Long, limit: Long) {
        val accounts = this.application.accountModels

        if (accounts.isEmpty()) {
            this.startActivityForResult(Intent(this.applicationContext, LoginActivity::class.java), 100)
        }

        this.listAdapter.items = accounts.toMutableList()
        this.listAdapter.notifyDataSetChanged()
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Account) {
            return
        }

        val intent = Intent(this, IndexActivity::class.java)
        intent.putExtra(ACCOUNT_KEY, item)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NEW_DOCUMENT

        this.startActivity(intent)
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Account) {
            return
        }

        (view.findViewById<View>(R.id.alias) as TextView).text = item.alias
        (view.findViewById<View>(R.id.url) as TextView).text = item.url
        (view.findViewById<View>(R.id.user) as TextView).text = item.user
    }
}