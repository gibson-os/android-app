package de.wollis_page.gibsonos.module.growDiary.setup.fragment

import android.os.Bundle
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.fragment.GibsonOsFragment
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask

class OverviewFragment: GibsonOsFragment() {
    override fun getContentView() = R.layout.grow_diary_setup_overview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.activity.runTask({
            val setup = SetupTask.get(this.activity, this.fragmentsArguments["setupId"].toString().toLong())
            this.activity.setTitle(setup.name)

            this.view?.findViewById<TextView>(R.id.name)?.text = setup.name
        })
    }
}