package de.wollis_page.gibsonos.module.growDiary.setup.fragment.climateControl

import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.setup.climateControl.Runtime
import de.wollis_page.gibsonos.module.growDiary.task.SetupTask

class RuntimeFragment: ListFragment() {
    override fun onClick(item: ListItemInterface) {
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Runtime) {
            return
        }

        view.findViewById<TextView>(R.id.from).text = item.from
        view.findViewById<TextView>(R.id.to).text = item.to
    }

    override fun getListRessource() = R.layout.grow_diary_setup_runtime_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(SetupTask.getClimateControlRuntimes(
            this,
            this.fragmentsArguments["climateControlId"].toString().toLong(),
            start,
            limit,
        ))
    }

    override fun actionButton() = R.layout.base_button_power

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.powerButton)

    override fun actionOnClickListener() {
        this.runTask({
            SetupTask.postSwitchClimateControl(this.activity, this.fragmentsArguments["climateControlId"].toString().toLong())
            this.loadList()
        })
    }
}