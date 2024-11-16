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
import de.wollis_page.gibsonos.module.growDiary.index.dto.Plant
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class PlantFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Plant>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                PlantTask.getImage(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            {
                this.getViewByItem(it)?.findViewById(R.id.image)
            }
        )

//        val inflater = LayoutInflater.from(this.activity)
//        this.activity.contentContainer.addView(inflater.inflate(
//            R.layout.base_button_add,
//            this.activity.findViewById(android.R.id.content),
//            false
//        ))
//        val addButton = this.activity.findViewById<FloatingActionButton>(R.id.addButton)
//        addButton.setOnClickListener {
//            startActivityForResult(
//                Intent(this.activity.applicationContext, LoginActivity::class.java),
//                100
//            )
//        }
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Plant) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "plant",
                    "index",
                    mapOf(
                        "plantId" to item.id,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Plant) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.seed).text = item.seed!!.name

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(PlantTask.list(this.activity, start, limit))
    }

    override fun getListRessource() = R.layout.grow_diary_plant_list_item

    private fun getShortcut(item: Plant): Shortcut {
        return Shortcut(
            "growDiary",
            "plant",
            "index",
            item.name,
            "icon_hemp",
            mutableMapOf(
                "plantId" to item.id,
                "name" to item.name,
            )
        )
    }
}