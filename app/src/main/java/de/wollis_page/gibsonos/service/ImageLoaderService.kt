package de.wollis_page.gibsonos.service

import android.graphics.Bitmap
import android.widget.ImageView
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.exception.ResponseException
import de.wollis_page.gibsonos.exception.TaskException

class ImageLoaderService<T : ListItemInterface>(
    val activity: GibsonOsActivity,
    val loadFunction: (item: T) -> Bitmap?,
    val viewFunction: (item: T, image: Bitmap) -> Unit?,
) {
    private var images = HashMap<Any, Bitmap>()
    private var queue = ArrayList<T>()
    private var imagesLoading = 0
    var maxLoadingImages = 5

    fun getImage(item: T): Bitmap? {
        val image = this.images[item.getId()]

        if (image != null) {
            return image
        }

        if (this.queue.find { it.equals(item) } == null) {
            this.addQueue(item)
        }

        return null
    }

    fun viewImage(item: T, imageView: ImageView, placeholderResource: Int? = null) {
        val image = this.getImage(item)

        if (image == null) {
            placeholderResource?.let { imageView.setImageResource(it) }
        } else {
            imageView.setImageBitmap(image)
        }
    }

    fun addQueue(item: T) {
        this.queue.add(0, item)
        this.loadImages()
    }

    private fun loadImages() {
        if (this.imagesLoading == this.maxLoadingImages) {
            return
        }

        this.imagesLoading++

        this.activity.runTask({
            while (this.queue.size != 0) {
                val item = this.queue.first()
                this.queue.removeAt(0)
                val itemId = item.getId()

                try {
                    if (this.images[itemId] == null) {
                        this.loadFunction(item)?.let { this.images[itemId] = it }
                    }

                    val image = this.images[itemId]

                    if (image != null) {
                        this.activity.runOnUiThread { this.viewFunction(item, image) }
                    }
                } catch (_: TaskException) {
                } catch (_: ResponseException) {
                }
            }

            this.imagesLoading--
        })
    }
}