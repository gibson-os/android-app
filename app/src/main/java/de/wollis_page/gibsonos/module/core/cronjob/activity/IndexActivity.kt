package de.wollis_page.gibsonos.module.core.cronjob.activity

import android.view.View
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.AppActivityInterface
import de.wollis_page.gibsonos.activity.ListActivity
import de.wollis_page.gibsonos.dto.ListItemInterface

class IndexActivity: ListActivity(), AppActivityInterface {
    override fun onClick(item: ListItemInterface) {
        TODO("Not yet implemented")
    }

    override fun bind(item: ListItemInterface, view: View) {
        TODO("Not yet implemented")
    }

    override fun getListRessource(): Int {
        TODO("Not yet implemented")
    }

    override fun getAppIcon() = R.drawable.ic_calendar_alt
}