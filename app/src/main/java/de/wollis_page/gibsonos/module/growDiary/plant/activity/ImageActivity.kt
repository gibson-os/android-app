package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Image
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.AppIntentExtraService
import de.wollis_page.gibsonos.service.ImageLoaderService
import de.wollis_page.gibsonos.view.TouchImageView
import java.io.File
import java.io.FileOutputStream


class ImageActivity: GibsonOsActivity() {
    private lateinit var imageLoaderService: ImageLoaderService<Image>
    private lateinit var image: Image
    private var plantId: Long? = null
    private var images: MutableList<Image> = mutableListOf()
    private var beforeCompleteLoaded = false
    private var afterCompleteLoaded = false

    override fun getContentView() = R.layout.grow_diary_plant_image_view

    override fun getId() = this.plantId ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView = this.findViewById<TouchImageView>(R.id.image)

        this.plantId = this.intent.getLongExtra("plantId", 0)
        this.image = AppIntentExtraService.getIntentExtra("image", this.intent) as Image
        this.images.add(this.image)

        this.loadImagesBefore()
        this.loadImagesAfter()

        this.setTitle(this.image.created + "(Tag " + this.image.day + ")")

        imageView.onSwipeLeft = {
            val index = this.images.indexOf(image)

            if (index != 0) {
                this.image = this.images[index-1]
                this.imageLoaderService.viewImage(this.image, imageView, R.drawable.ic_hemp)
                this.setTitle(this.image.created)
            }

            if (index == 1) {
                this.loadImagesBefore()
            }
        }
        imageView.onSwipeRight = {
            val index = this.images.indexOf(image)

            if (index != this.images.lastIndex) {
                this.image = this.images[index+1]
                this.imageLoaderService.viewImage(this.image, imageView, R.drawable.ic_hemp)
                this.setTitle(this.image.created)
            }

            if (index + 1 == this.images.lastIndex) {
                this.loadImagesAfter()
            }
        }

        this.imageLoaderService = ImageLoaderService(
            this,
            {
                PlantTask.getImage(
                    this,
                    this.plantId ?: 0,
                    null,
                    null,
                    this.image.created,
                )
            },
            { plantImage, image ->
                if (plantImage.id == this.image.id) {
                    imageView.setImageBitmap(image)
                }
            }
        )
        this.imageLoaderService.viewImage(this.image, imageView, R.drawable.ic_hemp)

        val inflater = LayoutInflater.from(this)
        this.contentContainer.addView(inflater.inflate(
            R.layout.base_button_share,
            this.findViewById(android.R.id.content),
            false
        ))
        val shareButton = findViewById<FloatingActionButton>(R.id.shareButton)
        shareButton.setOnClickListener {
            val cachePath = cacheDir
            cachePath.mkdirs()

            val shareFile = File(cachePath, "sharedImage.jpg")
            val stream = FileOutputStream(shareFile)
            val sharetBitmap: Bitmap = this.imageLoaderService.getImage(image)!!
            sharetBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.close()

            val newFile = File(cachePath, "sharedImage.jpg")
            val contentUri = FileProvider.getUriForFile(
                applicationContext,
                "${applicationContext.packageName}.fileprovider",
                newFile
            )

            if (contentUri != null) {
                val shareIntent = Intent()
                shareIntent.setAction(Intent.ACTION_SEND)
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, this.image.created)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
            }
        }
    }

    private fun loadImagesAfter() {
        if (this.afterCompleteLoaded) {
            return
        }

        this.runTask({
            val images = PlantTask.getImagesAfter(this, this.image.id, 10).data
            this.images.addAll(images)
            this.afterCompleteLoaded = images.count() != 10
        })
    }

    private fun loadImagesBefore() {
        if (this.beforeCompleteLoaded) {
            return
        }

        this.runTask({
            val images = PlantTask.getImagesBefore(this, this.image.id, 10).data

            this.images.addAll(0, images.reversed())
            this.beforeCompleteLoaded = images.count() != 10
        })
    }
}