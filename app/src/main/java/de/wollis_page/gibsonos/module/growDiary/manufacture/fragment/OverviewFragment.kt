package de.wollis_page.gibsonos.module.growDiary.manufacture.fragment

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.task.ManufactureTask

class OverviewFragment: GibsonOsFragment() {
    override fun getContentView() = R.layout.grow_diary_manufacture_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val manufacture = ManufactureTask.get(this.activity, this.fragmentsArguments["manufactureId"].toString().toLong())

            this.activity.runOnUiThread {
                this.view?.findViewById<TextView>(R.id.name)?.text = manufacture.name
                this.view?.findViewById<TextView>(R.id.url)?.text = manufacture.url
            }

            val imageView = this.view?.findViewById<ImageView>(R.id.image)
            val image = ManufactureTask.image(this.activity, manufacture.id, imageView?.width)

            this.activity.runOnUiThread {
                imageView?.setImageBitmap(image)
            }
        })
    }
}