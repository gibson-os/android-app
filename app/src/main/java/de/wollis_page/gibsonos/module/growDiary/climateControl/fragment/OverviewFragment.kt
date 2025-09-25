package de.wollis_page.gibsonos.module.growDiary.climateControl.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.HeadlineBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ImageBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ManufactureBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TextBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TitleBuilder
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.ClimateControlTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class OverviewFragment: AbstractOverviewFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.reloadOverviewModel()
        }
    }

    override fun loadOverviewModel() {
        this.activity.runTask({
            val climateControl = ClimateControlTask.get(this.activity, this.fragmentsArguments["climateControlId"].toString().toLong())
            val image = ClimateControlTask.image(this.activity, climateControl.id, this.view?.findViewById<ImageView>(R.id.image)?.width)
            val viewModel = this.viewModel

            this.activity.runOnUiThread {
                viewModel.addItem(TitleBuilder(climateControl.name))
                viewModel.addItem(HeadlineBuilder(climateControl.name))
                viewModel.addItem(ManufactureBuilder(climateControl.manufacture))
                viewModel.addItem(TextBuilder(climateControl.type, this.getString(R.string.grow_diary_climate_control_type)))
                viewModel.addItem(TextBuilder("${climateControl.watt}W", this.getString(R.string.grow_diary_watt)))
                viewModel.addItem(ImageBuilder(image))
            }
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
                    "task" to "climateControl",
                    "id" to this.fragmentsArguments["climateControlId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.editButton)
}