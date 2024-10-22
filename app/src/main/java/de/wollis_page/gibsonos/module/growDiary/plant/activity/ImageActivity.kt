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
            val image = PlantTask.image(
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

//                if (newX < 0) {
//                    newX = 0F
//                }

//                if (newX > imageView.width.toFloat()) {
//                    newX = imageView.width.toFloat()
//                }

//                if (newY < 0) {
//                    newY = 0F
//                }

//                if (newY > imageView.height.toFloat()) {
//                    newY = imageView.height.toFloat()
//                }

                imageView.x = newX
                imageView.y = newY

//                Log.d(Config.LOG_TAG, imageView.x.toString())
//                Log.d(Config.LOG_TAG, imageView.width.toString())
//                Log.d(Config.LOG_TAG, imageView.y.toString())

                this.previousX = event.x
                this.previousY = event.y
            }
        }

        return super.dispatchTouchEvent(event)
    }
}