package de.wollis_page.gibsonos.module.growDiary.setup.fragment.light

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.light.PlannedRuntime
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class PlannedRuntimeFragment: ListFragment() {
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
        if (item !is PlannedRuntime) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "setup",
                "lightPlannedRuntimeForm",
                mapOf(
                    "plannedRuntimeId" to item.id,
                    "lightId" to this.fragmentsArguments["lightId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is PlannedRuntime) {
            return
        }

        view.findViewById<TextView>(R.id.from).text = item.from
        view.findViewById<TextView>(R.id.to).text = item.to
    }

    override fun getListRessource() = R.layout.grow_diary_setup_runtime_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(SetupTask.getPlannedLightRuntimes(
            this,
            this.fragmentsArguments["lightId"].toString().toLong(),
            start,
            limit,
        ))
    }

    override fun actionButton() = R.layout.base_button_add

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)

    override fun actionOnClickListener() {
        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "setup",
                "lightPlannedRuntimeForm",
                mapOf(
                    "lightId" to this.fragmentsArguments["lightId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }
}