package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.FileProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.view.TouchImageView
import java.io.File
import java.io.FileOutputStream


class ImageActivity: GibsonOsActivity() {
    private var plantId: Long? = null
    private var created: String? = null
    private lateinit var image: Bitmap

    override fun getContentView() = R.layout.grow_diary_plant_image_view

    override fun getId() = this.plantId ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView = this.findViewById<TouchImageView>(R.id.image)

        this.plantId = this.intent.getLongExtra("plantId", 0)
        this.created = this.intent.getStringExtra("created")

        this.setTitle(this.created)

        this.runTask({
            this.image = PlantTask.getImage(
                this,
                this.plantId ?: 0,
                null,
                null,
                this.created,
            )

            this.runOnUiThread {
                imageView.setImageBitmap(image)
            }
        })

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
            val sharetBitmap: Bitmap = image
            sharetBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
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
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.grow_diary_title))
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                startActivity(Intent.createChooser(shareIntent, getString(R.string.share_image)))
            }
        }
    }
}