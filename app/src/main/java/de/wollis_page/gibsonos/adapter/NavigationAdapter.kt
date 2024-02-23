package de.wollis_page.gibsonos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.NavigationItem
import de.wollis_page.gibsonos.service.AppIconService

class NavigationAdapter(
    context: Context,
    private val navigationItems: List<NavigationItem>,
    private val layoutResource: Int = R.layout.base_navigation_item,
): ArrayAdapter<NavigationItem>(context, layoutResource, navigationItems) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView

        if (view == null) {
            view = LayoutInflater.from(this.context).inflate(
                this.layoutResource,
                parent,
                false
            )!!
        }

        val navigationItem = this.navigationItems[position]
        val textView = view.findViewById<TextView>(R.id.navigation_item_text)
        val iconView = view.findViewById<ImageView>(R.id.navigation_item_icon)

        textView.text = navigationItem.getText()
        iconView.visibility = View.GONE

        if (!navigationItem.isAccount()) {
            iconView.setImageResource(AppIconService.getIcon(
                navigationItem.getModule(),
                navigationItem.getTask(),
                navigationItem.getAction(),
            ) ?: R.drawable.ic_android)
            iconView.visibility = View.VISIBLE
        }

        return view
    }
}