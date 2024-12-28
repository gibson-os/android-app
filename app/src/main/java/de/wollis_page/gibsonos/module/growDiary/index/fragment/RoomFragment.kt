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
import de.wollis_page.gibsonos.module.growDiary.index.dto.Room
import de.wollis_page.gibsonos.module.growDiary.task.RoomTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class RoomFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Room>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                RoomTask.image(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { room, image ->
                this.getViewByItem(room)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Room) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "room",
                    "index",
                    mapOf(
                        "roomId" to item.id,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Room) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.size).text = "${item.length}cm X ${item.width}cm X ${item.height}cm"

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(RoomTask.list(
            this.activity,
            start,
            limit,
            this.fragmentsArguments["manufactureId"]?.toString()?.toLong(),
        ))
    }

    override fun getListRessource() = R.layout.grow_diary_room_list_item

    private fun getShortcut(item: Room): Shortcut {
        return Shortcut(
            "growDiary",
            "room",
            "index",
            item.name,
            "icon_hemp",
            mutableMapOf(
                "roomId" to item.id,
                "name" to item.name,
            )
        )
    }
}