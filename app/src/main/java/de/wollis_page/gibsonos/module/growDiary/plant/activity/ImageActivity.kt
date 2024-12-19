package de.wollis_page.gibsonos.module.growDiary.plant.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.ImageView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.service.ScaleListener


class ImageActivity: GibsonOsActivity() {
    private var plantId: Long? = null
    private var created: String? = null
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var previousX: Float = 0F
    private var previousY: Float = 0F

    override fun getContentView() = R.layout.grow_diary_plant_image_view

    override fun getId(): Long = this.plantId ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageView = this.findViewById<ImageView>(R.id.image)
        this.scaleGestureDetector = ScaleGestureDetector(this, ScaleListener(imageView))

        this.plantId = this.intent.getLongExtra("plantId", 0)
        this.created = this.intent.getStringExtra("created")

        this.runTask({
            val image = PlantTask.getImage(
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
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        this.scaleGestureDetector.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.previousX = event.x
                this.previousY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val imageView = this.findViewById<ImageView>(R.id.image)
                var newX = imageView.x + (event.x - this.previousX)
                var newY = imageView.y + (event.y - this.previousY)

                val imageWidth = imageView.width.toFloat()
                val maxX = (imageView.scaleX - 1) * (imageWidth / 2)
                val minX = 0 - maxX

                if (newX < minX) {
                    newX = minX
                } else if (newX > maxX) {
                    newX = maxX
                }

                val imageHeight = imageView.height.toFloat()
                val maxY = (imageView.scaleY - 1) * (imageHeight / 2)
                val minY = 0 - maxY

                if (newY < minY) {
                    newY = minY
                } else if (newY > maxY) {
                    newY = maxY
                }

                imageView.x = newX
                imageView.y = newY

                this.previousX = event.x
                this.previousY = event.y
            }
        }

        return super.dispatchTouchEvent(event)
    }
}