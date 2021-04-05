package de.wollis_page.gibsonos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.desktop.Item

class DesktopAdapter(context: Context?, desktop: List<Item>) : BaseAdapter() {
    private val desktop: List<Item>
    private val inflater: LayoutInflater

    override fun getCount(): Int {
        return desktop.size
    }

    override fun getItem(i: Int): Any {
        return desktop[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, convertView: View, viewGroup: ViewGroup): View {
        val view = inflater.inflate(R.layout.desktop_list_item, viewGroup, false)
        (view.findViewById<View>(R.id.text) as TextView).text = desktop[i].text
        return view
    }

    init {
        inflater = LayoutInflater.from(context)
        this.desktop = desktop
    }
}