package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Setup
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class SetupFragment: ListFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.loadList()
        }
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Setup) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "setup",
                    "index",
                    mapOf(
                        "setupId" to item.id,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Setup) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(SetupTask.getList(
            this.activity,
            start,
            limit,
        ))
    }

    override fun getListRessource() = R.layout.grow_diary_setup_list_item

    private fun getShortcut(item: Setup): Shortcut {
        return Shortcut(
            "growDiary",
            "setup",
            "index",
            item.name,
            "icon_hemp",
            mutableMapOf(
                "setupId" to item.id,
                "name" to item.name,
            )
        )
    }

    override fun actionButton() = R.layout.base_button_add

    override fun actionOnClickListener() {
        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "index",
                "form",
                mapOf(
                    "task" to "setup",
                ),
                this.formLauncher,
            )
        })
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)
}