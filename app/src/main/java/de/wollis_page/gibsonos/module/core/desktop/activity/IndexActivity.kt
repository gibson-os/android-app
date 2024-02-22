package de.wollis_page.gibsonos.module.core.desktop.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.module.core.desktop.dialog.ItemDialog
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.core.task.DesktopTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.AppIconService

class IndexActivity : ListActivity() {
    override fun getListRessource() = R.layout.desktop_list_item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTitle()

        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            R.layout.base_button_cog,
            this.findViewById(android.R.id.content),
            false
        ))

        val itemDialog = ItemDialog(this).build()
        val cogButton = this.findViewById<FloatingActionButton>(R.id.cogButton)
        cogButton.setOnClickListener {
            itemDialog.show()
        }
    }

    override fun getId(): Any = this.getAccount().id

    override fun isActivityforShotcut(shortcut: Shortcut): Boolean = false

    override fun loadList(start: Long, limit: Long) = this.load {
        val desktop = DesktopTask.index(this)

        it.apps = desktop.apps
        this.listAdapter.items = desktop.desktop as ArrayList<ListItemInterface>
        this.navigationService.loadNavigation()
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Shortcut) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(this, item, mapOf(SHORTCUT_KEY to item))
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Shortcut) {
            return
        }

        view.findViewById<TextView>(R.id.text).text = item.text
        view.findViewById<ImageView>(R.id.icon).setImageResource(
            AppIconService.getIcon(item.module, item.task, "index") ?: R.drawable.ic_android
        )
    }

    override fun deleteItem(item: ListItemInterface): Boolean {
        if (item !is Shortcut) {
            return false
        }

        val items = this.listAdapter.items as ArrayList<Shortcut>

        this.runTask({
            this.listAdapter.items = DesktopTask.save(this, items) as ArrayList<ListItemInterface>
            this.listAdapter.notifyDataSetChanged()
        })

        return true
    }

    override fun getDeleteTitle() = this.getString(R.string.core_desktop_delete_title)

    override fun getDeleteMessage(item: ListItemInterface) = this.getString(
        R.string.core_desktop_delete_message,
        if (item is Shortcut) item.text else ""
    )
}