package de.wollis_page.gibsonos.module.core.message.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.orm.SugarRecord
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.model.Message
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.service.AppIconService

class IndexActivity: ListActivity() {
    override fun getId(): Any = 0

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.core_message_title)
    }

    override fun onClick(item: ListItemInterface) {
        TODO("Not yet implemented")
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Message) {
            return
        }

        (view.findViewById<View>(R.id.title) as TextView).text = item.title
        (view.findViewById<View>(R.id.body) as TextView).text = item.body
        view.findViewById<ImageView>(R.id.icon).setImageResource(
            AppIconService.getIcon(item.module, item.task, item.action) ?: R.drawable.ic_android
        )
        (view.findViewById<View>(R.id.date) as TextView).text = item.date
    }

    override fun getListRessource(): Int = R.layout.core_message_index_list_item

    override fun loadList(start: Long, limit: Long) {
        val messages = SugarRecord.findWithQuery(
            Message::class.java,
            "SELECT * FROM Message WHERE account=?",
            this.getAccount().id.toString()
        )
//        val messages = SugarRecord.findWithQuery(
//            Message::class.java,
//            "SELECT * FROM Message",
//        )

        this.listAdapter.items = messages as ArrayList<ListItemInterface>
        this.listAdapter.notifyItemRangeChanged(0, messages.size)
    }
}