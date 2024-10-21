package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Manufacture
import de.wollis_page.gibsonos.module.growDiary.task.ManufactureTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class ManufactureFragment: ListFragment() {
    private var images = HashMap<Long, Bitmap>()
    private var imageQueue = ArrayList<Manufacture>()
    private var imagesLoading = false

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

        val imageView = view.findViewById<ImageView>(R.id.image)

        if (this.images[item.id] != null) {
            imageView.setImageBitmap(this.images[item.id])
        }
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        val manufactures = ManufactureTask.list(this.activity, start, limit)

        manufactures.data.forEach {
            this.imageQueue.add(it)
        }

        this.loadImages()
        this.listAdapter.setListResponse(manufactures)
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

    private fun loadImages() {
        if (this.imagesLoading) {
            return
        }

        this.imagesLoading = true

        this.runTask({
            while (this.imageQueue.size != 0) {
                val item = this.imageQueue.first()
                this.imageQueue.removeAt(0)

                try {
                    if (this.images[item.id] == null) {
                        this.images[item.id] = ManufactureTask.image(
                            this.activity,
                            item.id,
                            resources.getDimension(R.dimen.thumb_width).toInt()
                        )
                    }

                    val imageView = this.getViewByItem(item)?.findViewById<ImageView>(R.id.image)

                    if (imageView === null) {
                        continue
                    }

                    this.activity.runOnUiThread { imageView.setImageBitmap(this.images[item.id]) }
                } catch (_: TaskException) {
                } catch (_: ResponseException) {
                }
            }

            this.imagesLoading = false
        })
    }
}