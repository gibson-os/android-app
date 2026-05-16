package de.wollis_page.gibsonos.module.tc.index.activity

import android.os.Bundle
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.activity.TabActivity
import de.wollis_page.gibsonos.dto.Tab
import de.wollis_page.gibsonos.module.tc.index.fragment.TrainFragment

class IndexActivity : TabActivity() {
    override fun getTabs(): Array<Tab> = arrayOf(
        Tab(R.string.tc_train_tab, TrainFragment::class),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.setTitle(R.string.tc_title)
    }

    override fun getId() = 0

}