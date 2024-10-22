package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.os.Bundle
import android.widget.ImageView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask

class ImageActivity: GibsonOsActivity() {
    private var plantId: Long? = null
    private var created: String? = null

    override fun getContentView() = R.layout.grow_diary_plant_image_view

    override fun getId(): Long = this.plantId ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.plantId = this.intent.getLongExtra("plantId", 0)
        this.created = this.intent.getStringExtra("created")

        this.runTask({
            val image = PlantTask.image(
                this,
                this.plantId ?: 0,
                null,
                null,
                this.created,
            )

            this.runOnUiThread {
                this.findViewById<ImageView>(R.id.image).setImageBitmap(image)
            }
        })
    }
}