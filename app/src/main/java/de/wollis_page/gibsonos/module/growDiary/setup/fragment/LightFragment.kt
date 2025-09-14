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
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.Light
import de.wollis_page.gibsonos.module.growDiary.task.LightTask
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class LightFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Light>
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                LightTask.image(
                    this.activity,
                    it.light.id,
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
        if (item !is Light) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "setup",
                "light",
                mapOf(
                    "lightId" to item.id,
                ),
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Light) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.light.name
        view.findViewById<TextView>(R.id.watt).text = "${item.light.watt}W"

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun getListRessource() = R.layout.grow_diary_setup_light_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(SetupTask.getLights(
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
                "setup",
                "lightForm",
                mapOf(
                    "setupId" to this.fragmentsArguments["setupId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }
}