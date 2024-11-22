package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.view.View
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.task.FeedTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class FeedFragement: ListFragment() {
    override fun onClick(item: ListItemInterface) {
        if (item !is Feed) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "feed",
                "form",
                mapOf(
                    "plantId" to this.fragmentsArguments["plantId"].toString().toLong(),
                    "feedId" to item.id,
                )
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Feed) {
            return
        }

        view.findViewById<TextView>(R.id.added).text = item.added
        view.findViewById<TextView>(R.id.milliliter).text = item.milliliter.toString() + "ml"
//        view.findViewById<TextView>(R.id.additives).text = item.relativeHumidity.toString() + "%"

    }

    override fun getListRessource() = R.layout.grow_diary_plant_feed_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(
            FeedTask.list(
            this.activity,
            this.fragmentsArguments["plantId"].toString().toLong(),
            start,
            limit,
        ))
    }

    override fun actionButton() = R.layout.base_button_add

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)

    override fun actionOnClickListener() {
        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "feed",
                "form",
                mapOf(
                    "plantId" to this.fragmentsArguments["plantId"].toString().toLong(),
                )
            )
        })
    }
}