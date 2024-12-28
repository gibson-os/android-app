package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Fertilizer
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class FertilizerFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Fertilizer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                FertilizerTask.getImage(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { fertilizer, image ->
                this.getViewByItem(fertilizer)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Fertilizer) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "fertilizer",
                    "index",
                    mapOf(
                        "fertilizerId" to item.id,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Fertilizer) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(FertilizerTask.list(
            this.activity,
            start,
            limit,
            this.fragmentsArguments["manufactureId"]?.toString()?.toLong(),
        ))
    }

    override fun getListRessource() = R.layout.grow_diary_fertilizer_list_item

    private fun getShortcut(item: Fertilizer): Shortcut {
        return Shortcut(
            "growDiary",
            "fertilizer",
            "index",
            item.name,
            "icon_hemp",
            mutableMapOf(
                "fertilizerId" to item.id,
                "name" to item.name,
            )
        )
    }
}