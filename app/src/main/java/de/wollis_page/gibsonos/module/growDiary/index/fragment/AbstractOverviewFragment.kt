package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment

abstract class AbstractOverviewFragment: GibsonOsFragment() {
    protected lateinit var inflater: LayoutInflater

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

    protected fun addOverviewItem(label: Int, value: String) {
        this.getOverviewContainer().addView(this.getOverviewItem(label, value))
    }

    protected fun addOverviewItem(label: String, value: String) {
        this.getOverviewContainer().addView(this.getOverviewItem(label, value))
    }

    protected fun getOverviewHeadline(title: Int) = this.getOverviewHeadline(this.getString(title))

    protected fun getOverviewHeadline(title: String): View {
        val overviewItem = this.inflater.inflate(
            R.layout.grow_diary_overview_headline,
            this.view?.findViewById(android.R.id.content),
            false
        )

        overviewItem.findViewById<TextView>(R.id.title).text = title

        return overviewItem
    }

    protected fun getOverviewSubheadline(title: Int) = this.getOverviewSubeadline(this.getString(title))

    protected fun getOverviewSubeadline(title: String): View {
        val overviewItem = this.inflater.inflate(
            R.layout.grow_diary_overview_subheadline,
            this.view?.findViewById(android.R.id.content),
            false
        )

        overviewItem.findViewById<TextView>(R.id.title).text = title

        return overviewItem
    }

    protected fun getOverviewItem(label: Int, value: String) = this.getOverviewItem(this.getString(label), value)

    protected fun getOverviewItem(label: String, value: String): View {
        val overviewItem = this.inflater.inflate(
            R.layout.grow_diary_overview_item,
            this.view?.findViewById(android.R.id.content),
            false
        )

        overviewItem.findViewById<TextView>(R.id.label).text = label
        overviewItem.findViewById<TextView>(R.id.value).text = value

        return overviewItem
    }

    protected fun getOverviewRangeItem(
        label: Int,
        min: Any?,
        max: Any?,
        differentValue: String,
        sameValue: String,
    ): View {
        var value = differentValue

        if (min == max) {
            value = sameValue
        }

        return this.getOverviewItem(label, value)
    }

    protected fun addOverviewRangeItem(
        label: Int,
        min: Any?,
        max: Any?,
        differentValue: String,
        sameValue: String,
    ) {
        var value = differentValue

        if (min == max) {
            value = sameValue
        }

        this.addOverviewItem(label, value)
    }
}