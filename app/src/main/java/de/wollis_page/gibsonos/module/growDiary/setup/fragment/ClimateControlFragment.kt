package de.wollis_page.gibsonos.module.growDiary.setup.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.ClimateControl
import de.wollis_page.gibsonos.module.growDiary.task.ClimateControlTask
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class ClimateControlFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<ClimateControl>
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                ClimateControlTask.image(
                    this.activity,
                    it.climateControl.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { climateControl, image ->
                this.getViewByItem(climateControl)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )

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
        if (item !is ClimateControl) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "setup",
                "climateControl",
                mapOf(
                    "climateControlId" to item.id,
                ),
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is ClimateControl) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.climateControl.name
        view.findViewById<TextView>(R.id.useCase).text = item.useCase

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun getListRessource() = R.layout.grow_diary_setup_climate_control_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(SetupTask.getClimateControls(
            this,
            this.fragmentsArguments["setupId"].toString().toLong(),
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
                "index",
                "form",
                mapOf(
                    "task" to "setup",
                    "action" to "climateControlForm",
                    "additionalParameters" to hashMapOf(
                        "setupId" to this.fragmentsArguments["setupId"].toString(),
                    ),
                ),
                this.formLauncher,
            )
        })
    }
}