package de.wollis_page.gibsonos.module.tc.index.fragment

import android.view.View
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.tc.index.dto.Train
import de.wollis_page.gibsonos.module.tc.task.TrainTask

class TrainFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Train) {
            return
        }

//        this.runTask({
//            try {
//                ActivityLauncherService.startActivity(
//                    this.activity,
//                    "hc",
//                    "index",
//                    "master",
//                    mapOf<String, Parcelable>(
//                        "master" to item,
//                        GibsonOsActivity.SHORTCUT_KEY to this.getShortcut(item),
//                    )
//                )
//            } catch (exception: ClassNotFoundException) {
//                throw AppException("Not implemented yet!", R.string.not_implemented_yet)
//            }
//        })
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