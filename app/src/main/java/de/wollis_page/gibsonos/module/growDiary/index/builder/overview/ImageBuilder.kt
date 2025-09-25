package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class ImageBuilder(private val image: Bitmap): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        val imageView = view.findViewById<ImageView>(R.id.image)
        imageView.visibility = View.VISIBLE

        activity.runOnUiThread {
            imageView?.setImageBitmap(image)
        }
    }
}