package de.wollis_page.gibsonos.module.growDiary.index.fragment

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
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.Light
import de.wollis_page.gibsonos.module.growDiary.task.LightTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class LightFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Light>
    lateinit var formLauncher: ActivityResultLauncher<Intent>

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
                LightTask.image(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt()
                )
            },
            { light, image ->
                this.getViewByItem(light)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Light) {
            return
        }

        this.runTask({
            try {
                ActivityLauncherService.startActivity(
                    this.activity,
                    "growDiary",
                    "light",
                    "index",
                    mapOf(
                        "lightId" to item.id,
                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
                    )
                )
            } catch (exception: ClassNotFoundException) {
                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
            }
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Light) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.watt).text = "${item.watt}W"

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(LightTask.list(
            this.activity,
            start,
            limit,
            this.fragmentsArguments["manufactureId"]?.toString()?.toLong(),
        ))
    }

    override fun getListRessource() = R.layout.grow_diary_light_list_item

    private fun getShortcut(item: Light): Shortcut {
        return Shortcut(
            "growDiary",
            "light",
            "index",
            item.name,
            "icon_hemp",
            mutableMapOf(
                "lightId" to item.id,
                "name" to item.name,
            )
        )
    }

    override fun actionButton() = R.layout.base_button_add

    override fun actionOnClickListener() {
        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "index",
                "form",
                mapOf(
                    "task" to "light",
                ),
                this.formLauncher,
            )
        })
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)
}