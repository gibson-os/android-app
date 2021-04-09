package de.wollis_page.gibsonos.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.base.GibsonOsActivity
import de.wollis_page.gibsonos.model.Account
import de.wollis_page.gibsonos.dto.desktop.Item
import de.wollis_page.gibsonos.helper.Config
import java.util.*
import kotlin.collections.ArrayList

class DesktopAdapter(context: Context, private val account: Account) : RecyclerView.Adapter<DesktopAdapter.ItemHolder>() {
    private val inflater = LayoutInflater.from(context)
    var desktop: MutableList<Item> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            this.inflater.inflate(R.layout.desktop_list_item, parent, false),
            this.account
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(this.desktop[position])
    }

    override fun getItemCount() = this.desktop.size

    class ItemHolder(private var view: View, private var account: Account) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var item: Item

        init {
            view.setOnClickListener(this)
        }

        fun bind(item: Item) {
            this.item = item

            (view.findViewById<View>(R.id.text) as TextView).text = item.text
            (view.findViewById<View>(R.id.module) as TextView).text = item.module + "_" + item.task
        }

        override fun onClick(p0: View?) {
            val context = this.itemView.context
            val packageName =
                "de.wollis_page.gibsonos.module." +
                this.item.module + ".activity." +
                this.item.task.capitalize(Locale.ROOT) + "Activity"

            try {
                Log.i(Config.LOG_TAG, "Look for package: $packageName")
                val activityClass = Class.forName(packageName)
                val intent = Intent(context, activityClass)
                intent.putExtra(GibsonOsActivity.ACCOUNT_KEY, this.account)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                context.startActivity(intent)
            } catch (exception: ClassNotFoundException) {
                Toast.makeText(context, R.string.not_implemented_yet, Toast.LENGTH_LONG).show()
            }

        }
    }
}