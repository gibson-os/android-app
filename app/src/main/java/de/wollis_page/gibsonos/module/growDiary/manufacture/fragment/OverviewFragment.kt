package de.wollis_page.gibsonos.module.growDiary.manufacture.fragment

import android.content.Intent
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.HeadlineBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ImageBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.LinkBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.TitleBuilder
import de.wollis_page.gibsonos.module.growDiary.index.fragment.AbstractOverviewFragment
import de.wollis_page.gibsonos.module.growDiary.task.ManufactureTask

class OverviewFragment: AbstractOverviewFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>

    override fun loadOverviewModel() {
        this.activity.runTask({
            val manufacture = ManufactureTask.get(this.activity, this.fragmentsArguments["manufactureId"].toString().toLong())
            val image = ManufactureTask.image(this.activity, manufacture.id, this.view?.findViewById<ImageView>(R.id.image)?.width)
            val viewModel = this.viewModel

            this.activity.runOnUiThread {
                viewModel.addItem(TitleBuilder(manufacture.name))
                viewModel.addItem(HeadlineBuilder(manufacture.name))
                viewModel.addItem(LinkBuilder(manufacture.url))
                viewModel.addItem(ImageBuilder(image))
            }
        })
    }
}