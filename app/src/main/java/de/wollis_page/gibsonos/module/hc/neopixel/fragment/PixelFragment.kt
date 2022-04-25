package de.wollis_page.gibsonos.module.hc.neopixel.fragment

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.neopixel.dto.Pixel
import de.wollis_page.gibsonos.module.hc.task.NeopixelTask

class PixelFragment : GibsonOsFragment() {
    lateinit var pixels: MutableList<Pixel>
    override fun getContentView(): Int = R.layout.hc_module_neopixel_pixel_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this.activity as ModuleActivity

        this.activity.runTask({
            this.pixels = NeopixelTask.pixels(activity, activity.module.id).data
        })
    }
}