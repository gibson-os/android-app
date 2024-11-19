package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Manufacture
import de.wollis_page.gibsonos.module.growDiary.task.ManufactureTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class ManufactureFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Manufacture>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                ManufactureTask.image(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            {
                this.getViewByItem(it)?.findViewById(R.id.image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Manufacture) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "manufacture",
                    "index",
                    mapOf(
                        "manufactureId" to item.id,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Manufacture) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.url).text = item.url

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(ManufactureTask.list(this.activity, start, limit))
    }

    override fun getListRessource() = R.layout.grow_diary_manufacture_list_item

    private fun getShortcut(item: Manufacture): Shortcut {
        return Shortcut(
            "growDiary",
            "manufacture",
            "index",
            item.name,
            "icon_hemp",
            mutableMapOf(
                "manufactureId" to item.id,
                "name" to item.name,
            )
        )
    }
}