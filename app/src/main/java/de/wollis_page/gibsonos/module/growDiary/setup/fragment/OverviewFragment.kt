package de.wollis_page.gibsonos.module.growDiary.setup.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class OverviewFragment: AbstractOverviewFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>
    override fun getContentView() = R.layout.grow_diary_setup_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.load()
        }

        this.load()
    }

    private fun load() {
        this.activity.runTask({
            val setup = SetupTask.get(this.activity, this.fragmentsArguments["setupId"].toString().toLong())
            this.activity.setTitle(setup.name)

            this.view?.findViewById<TextView>(R.id.name)?.text = setup.name
        })
    }

    override fun actionButton() = R.layout.base_button_edit

    override fun actionOnClickListener() {
        this.activity.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "index",
                "form",
                mapOf(
                    "task" to "setup",
                    "id" to this.fragmentsArguments["setupId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.editButton)
}