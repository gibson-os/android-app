package de.wollis_page.gibsonos.adapter

import android.annotation.SuppressLint
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
): ArrayAdapter<NavigationItem>(context, R.layout.base_navigation_item, navigationItems) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val navigationItem = this.navigationItems[position]
        val view = LayoutInflater.from(this.context).inflate(
            this.getLayoutResource(navigationItem),
            parent,
            false
        )!!

        view.findViewById<TextView>(R.id.navigation_item_text).text = navigationItem.getText()
        view.findViewById<ImageView?>(R.id.navigation_item_icon)?.setImageResource(AppIconService.getIcon(
            navigationItem.getModule(),
            navigationItem.getTask(),
            navigationItem.getAction(),
            ) ?: R.drawable.ic_android)

        return view
    }

    private fun getLayoutResource(navigationItem: NavigationItem): Int {
        if (navigationItem.isAccount()) {
            return R.layout.base_navigation_account_item
        }

        if (navigationItem.isApp()) {
            return R.layout.base_navigation_app_item
        }

        if (navigationItem.isShortcut()) {
            return R.layout.base_navigation_shortcut_item
        }

        return R.layout.base_navigation_item
    }
}