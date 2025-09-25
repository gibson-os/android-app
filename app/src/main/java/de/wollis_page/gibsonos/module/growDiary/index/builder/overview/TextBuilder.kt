package de.wollis_page.gibsonos.module.growDiary.index.builder.overview

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.GibsonOsActivity

class TextBuilder(
    private val text: String,
    private val label: String? = null,
    val onClickListener: View.OnClickListener? = null,
): OverviewBuilderInterface {
    override fun build(
        view: View,
        inflater: LayoutInflater,
        layout: LinearLayout,
        activity: GibsonOsActivity,
    ) {
        val overviewItem = inflater.inflate(
            R.layout.grow_diary_overview_item,
            view.findViewById(android.R.id.content),
            false
        )

        val label = this.label
        val labelView = overviewItem.findViewById<TextView>(R.id.label)

        labelView.visibility = View.GONE

        if (label !== null) {
            labelView.text = label
            labelView.visibility = View.VISIBLE
        }

        val onCLickListener = this.onClickListener
        val valueView = overviewItem.findViewById<TextView>(R.id.value)
        var valueText: CharSequence = this.text

        if (onCLickListener !== null) {
            valueText = SpannableString(this.text)
            valueText.setSpan(UnderlineSpan(), 0, valueText.length, 0)
            valueView.setOnClickListener(onCLickListener)
        }

        valueView.text = valueText

        layout.addView(overviewItem)
    }
}