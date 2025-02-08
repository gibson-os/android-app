package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.view.TouchImageView
import java.io.ByteArrayOutputStream
import java.io.File


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
            val share = Intent(Intent.ACTION_SEND)
            share.setType("image/jpeg")
            val tempFile: File

            val bytes = ByteArrayOutputStream()
            this.image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path: String = MediaStore.Images.Media.insertImage(
                this.contentResolver,
                this.image,
                "Share image",
                null
            )

            val fileName = Environment.getExternalStorageDirectory().path + File.separator + "temporary_file.jpg"

            this.contentResolver.openInputStream(Uri.parse(path))!!.use { input ->
                tempFile = File.createTempFile("sharedImage", ".jpg", this.cacheDir)

                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            share.putExtra(Intent.EXTRA_STREAM, Uri.parse(fileName))
            startActivity(Intent.createChooser(share, this.getString(R.string.share_image)))
        }
    }
}