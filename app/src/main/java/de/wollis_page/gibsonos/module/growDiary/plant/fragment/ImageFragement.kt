package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.GridFragment
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class ImageFragement: GridFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Image>
    private var plantId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.plantId = this.fragmentsArguments["plantId"].toString().toLong()

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                PlantTask.image(
                    this.activity,
                    this.plantId ?: 0,
                    this.view?.findViewById<ImageView>(R.id.image)?.width,
                    resources.getDimension(R.dimen.image_size).toInt(),
                    it.created,
                )
            },
            {
                this.getViewByItem(it)?.findViewById(R.id.image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Image) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "plant",
                    "image",
                    mapOf(
                        "plantId" to (this.plantId ?: 0),
                        "created" to item.created,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Image) {
            return
        }

        view.findViewById<TextView>(R.id.created).text = item.created

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun getListRessource() = R.layout.grow_diary_plant_image_card

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(PlantTask.images(
            this.activity,
            this.fragmentsArguments["plantId"].toString().toLong(),
            start,
            limit,
        ))
    }

    private fun getShortcut(item: Image): Shortcut {
        return Shortcut(
            "growDiary",
            "plant",
            "image",
            item.created,
            "icon_hemp",
            mutableMapOf(
                "plantId" to (this.plantId ?: 0),
                "created" to item.created,
            )
        )
    }
}