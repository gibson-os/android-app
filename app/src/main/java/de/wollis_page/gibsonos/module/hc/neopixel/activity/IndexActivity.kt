package de.wollis_page.gibsonos.module.hc.neopixel.activity

import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.hc.index.activity.ModuleActivity
import de.wollis_page.gibsonos.module.hc.neopixel.fragment.PixelFragment

class IndexActivity : ModuleActivity() {
    override fun getTabs(): Array<Tab> {
        val arguments = hashMapOf("module" to this.module)

        return arrayOf(
            Tab(R.string.hc_module_neopixel_pixel_tab, PixelFragment::class, arguments),
//            Tab(R.string.hc_module_neopixel_animation_tab, AnimationFragment::class, arguments),
            *super.getTabs()
        )
    }
}