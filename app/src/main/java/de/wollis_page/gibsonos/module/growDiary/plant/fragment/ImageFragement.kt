package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import de.wollis_page.gibsonos.fragment.GridFragment
import de.wollis_page.gibsonos.helper.Config
import de.wollis_page.gibsonos.module.core.desktop.dto.Shortcut
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ImageFragement: GridFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Image>
    private var plantId: Long? = null
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        this.activityResultLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != RESULT_OK) {
                return@registerForActivityResult
            }

            val imageBitmap = it.data?.extras?.get("data") as Bitmap
            Log.d(Config.LOG_TAG, "result")
            Log.d(Config.LOG_TAG, it.data.toString())
            Log.d(Config.LOG_TAG, it.toString())
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

            this.activity.runTask({
                PlantTask.postImage(
                    this.activity,
                    this.plantId!!,
                    imageBitmap,
                    LocalDateTime.now().format(dateFormatter),
                )
            })
        }

        super.onCreate(savedInstanceState)

        this.plantId = this.fragmentsArguments["plantId"].toString().toLong()

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                PlantTask.getImage(
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

        view.findViewById<TextView>(R.id.created).text = item.created + " (Tag " + item.day + ")"

        this.imageLoaderService.viewImage(
            item,
            view.findViewById(R.id.image),
            R.drawable.ic_hemp,
        )
    }

    override fun getListRessource() = R.layout.grow_diary_plant_image_card

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(PlantTask.getImages(
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

    override fun actionButton(): Int? {
        if (this.activity.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return R.layout.base_button_add
        }

        return null
    }

    override fun actionOnClickListener() {
        this.activityResultLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)
}