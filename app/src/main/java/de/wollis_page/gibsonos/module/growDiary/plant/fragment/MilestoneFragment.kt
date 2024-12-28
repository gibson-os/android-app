package de.wollis_page.gibsonos.module.growDiary.plant.fragment

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
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Milestone
import de.wollis_page.gibsonos.module.growDiary.task.MilestoneTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class MilestoneFragment: ListFragment() {
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
        if (item !is Milestone) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "milestone",
                "form",
                mapOf(
                    "plantId" to this.fragmentsArguments["plantId"].toString().toLong(),
                    "milestoneId" to item.id
                ),
                this.formLauncher,
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Milestone) {
            return
        }

        view.findViewById<TextView>(R.id.added).text = item.added
        view.findViewById<TextView>(R.id.title).text = item.title
        view.findViewById<TextView>(R.id.value).text = item.value
    }

    override fun getListRessource() = R.layout.grow_diary_plant_milestone_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(
            MilestoneTask.getList(
            this.activity,
            this.fragmentsArguments["plantId"].toString().toLong(),
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
                "milestone",
                "form",
                mapOf(
                    "plantId" to this.fragmentsArguments["plantId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }
}