package de.wollis_page.gibsonos.module.tc.index.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.tc.index.dto.Train
import de.wollis_page.gibsonos.module.tc.task.TrainTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class TrainFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Train) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "tc",
                "index",
                "form",
                mapOf(
                    "task" to "train",
                    "action" to "controlForm",
                    "id" to item.id,
                ),
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Train) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
    }

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(TrainTask.getList(this, start, limit))
    }

    override fun getListRessource() = R.layout.tc_train_list_item
}