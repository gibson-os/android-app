package de.wollis_page.gibsonos.service

import android.view.ScaleGestureDetector
import android.widget.ImageView

class ScaleListener internal constructor(
    private var imageView: ImageView,
) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
    private var scaleFactor = 1.0f

    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        this.scaleFactor *= scaleGestureDetector.scaleFactor
        this.scaleFactor = Math.max(
            1.0f,
            Math.min(this.scaleFactor, 100.0f),
        )
        this.imageView.scaleX = this.scaleFactor
        this.imageView.scaleY = this.scaleFactor

        return true
    }

}