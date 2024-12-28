package de.wollis_page.gibsonos.module.growDiary.plant.fragment

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
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Fertilizer
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class FertilizerFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Fertilizer>
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                FertilizerTask.getImage(
                    this.activity,
                    it.fertilizer.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { fertilizer, image ->
                this.getViewByItem(fertilizer)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
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
        if (item !is Fertilizer) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "plant",
                "Fertilizerform",
                mapOf(
                    "plantId" to this.fragmentsArguments["plantId"].toString().toLong(),
                    "fertilizerId" to item.id
                ),
                this.formLauncher,
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Fertilizer) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.fertilizer.name
        view.findViewById<TextView>(R.id.scheme).text = item.scheme.name

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun getListRessource() = R.layout.grow_diary_plant_fertilizer_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(
            PlantTask.getFertilizers(
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
                "plant",
                "fertilizerForm",
                mapOf(
                    "plantId" to this.fragmentsArguments["plantId"].toString().toLong(),
                ),
                this.formLauncher,
            )
        })
    }
}