package de.wollis_page.gibsonos.module.growDiary.plant.fragment.pot

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
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.pot.Substrate
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.module.growDiary.task.SubstrateTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService
import java.util.Locale

class SubstrateFragment: ListFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageLoaderService: ImageLoaderService<Substrate>

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

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                SubstrateTask.image(
                    this.activity,
                    it.substrate.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
                )
            },
            { substrate, image ->
                this.getViewByItem(substrate)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Substrate) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "index",
                "form",
                mapOf(
                    "task" to "plant",
                    "action" to "substrateForm",
                    "id" to item.id,
                    "additionalParameters" to hashMapOf(
                        "plantId" to this.fragmentsArguments["plantId"].toString(),
                        "potId" to this.fragmentsArguments["potId"].toString(),
                    ),
                ),
                this.formLauncher,
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Substrate) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.substrate.name
        view.findViewById<TextView>(R.id.liter).text = "${item.liter} l"
        view.findViewById<TextView>(R.id.pricePerUnit).text = String.format(Locale.getDefault(), "%.2f €/l", item.substrate.pricePerUnit.toFloat() / 100)

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun getListRessource() = R.layout.grow_diary_plant_pot_substrate_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(PlantTask.getSubstrates(
            this,
            this.fragmentsArguments["plantId"].toString().toLong(),
            this.fragmentsArguments["potId"].toString().toLong(),
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
                    "task" to "plant",
                    "action" to "substrateForm",
                    "additionalParameters" to hashMapOf(
                        "plantId" to this.fragmentsArguments["plantId"].toString(),
                        "potId" to this.fragmentsArguments["potId"].toString(),
                    ),
                ),
                this.formLauncher,
            )
        })
    }
}