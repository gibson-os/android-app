package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.ImageBuilder
import de.wollis_page.gibsonos.module.growDiary.index.builder.overview.OverviewBuilderInterface
import de.wollis_page.gibsonos.module.growDiary.index.model.OverviewModel

abstract class AbstractOverviewFragment: GibsonOsFragment() {
    protected lateinit var inflater: LayoutInflater
    protected val viewModel: OverviewModel by viewModels()

    protected abstract fun loadOverviewModel()

    override fun getContentView() = R.layout.grow_diary_overview

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        this.inflater = inflater

        return view
    }

    protected open fun getOverviewContainer(): LinearLayout = this.requireView().findViewById(R.id.overviewContainer)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.items.observe(viewLifecycleOwner, Observer { items ->
            this.rebuildUi(items)
        })

        if (this.viewModel.items.value === null) {
            this.loadOverviewModel()
        }
    }

    protected fun reloadOverviewModel() {
        this.viewModel.reset()
        this.loadOverviewModel()
        this.rebuildUi(this.viewModel.items.value ?: mutableListOf())
    }

    protected fun rebuildUi(items: List<OverviewBuilderInterface>) {
        this.getOverviewContainer().removeAllViews()

        val imageView = this.requireView().findViewById<ImageView>(R.id.image)
        imageView.visibility = View.VISIBLE
        var imageLoaded = false

        items.forEach {
            if (it is ImageBuilder) {
                imageLoaded = true
            }

            it.build(
                this.requireView(),
                this.inflater,
                this.getOverviewContainer(),
                this.activity,
            )
        }

        if (!imageLoaded) {
            imageView.visibility = View.GONE
        }
    }
}