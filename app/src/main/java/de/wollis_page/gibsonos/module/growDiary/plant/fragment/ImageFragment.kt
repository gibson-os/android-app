package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.AppException
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.fragment.GridFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService


class ImageFragment: GridFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Image>
    private var plantId: Long? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        this.pickMedia = this.registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            if (uris.isEmpty()) {
                return@registerForActivityResult
            }

            this.activity.runTask({
                uris.forEach { uri ->
                    try {
                        PlantTask.postImage(
                            this.activity,
                            this.fragmentsArguments["plantId"].toString().toLong(),
                            uri,
                        )
                    } catch (exception: ResponseException) {
                        if (exception.code != 409) {
                            throw exception
                        }

                        this.activity.runOnUiThread {
                            Toast.makeText(this.activity, exception.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                this.loadList()
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
            { plantImage, image ->
                this.getViewByItem(plantImage)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
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

    override fun actionButton() = R.layout.base_button_add

    override fun actionOnClickListener() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType("image/jpeg")))
    }

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)
}