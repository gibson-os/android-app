package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException
import de.wollis_page.gibsonos.fragment.GridFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask

class ImageFragement: GridFragment() {
    private var images = HashMap<Long, Bitmap>()
    private var imageQueue = ArrayList<Image>()
    private var imagesLoading = false

    override fun onClick(item: ListItemInterface) {
        TODO("Not yet implemented")
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Image) {
            return
        }

        view.findViewById<TextView>(R.id.created).text = item.created

        val imageView = view.findViewById<ImageView>(R.id.image)

        if (this.images[item.id] != null) {
            imageView.setImageBitmap(this.images[item.id])
        }
    }

    override fun getListRessource() = R.layout.grow_diary_plant_image_card

    override fun loadList(start: Long, limit: Long) = this.load {
        val images = PlantTask.images(
            this.activity,
            this.fragmentsArguments["plantId"].toString().toLong(),
            start,
            limit,
        )

        images.data.forEach {
            this.imageQueue.add(it)
        }

        this.loadImages()
        this.listAdapter.setListResponse(images)
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
                        this.images[item.id] = PlantTask.image(
                            this.activity,
                            this.fragmentsArguments["plantId"].toString().toLong(),
                            this.view?.findViewById<ImageView>(R.id.image)?.width,
                            resources.getDimension(R.dimen.image_size).toInt(),
                            item.created,
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