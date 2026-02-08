package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.children
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Fertilizer
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Feed
import de.wollis_page.gibsonos.module.growDiary.task.FeedTask
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask
import de.wollis_page.gibsonos.service.ActivityLauncherService
import de.wollis_page.gibsonos.service.ImageLoaderService

class FeedFragment: ListFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageLoaderService: ImageLoaderService<Fertilizer>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.formLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode != Activity.RESULT_OK) {
                return@registerForActivityResult
            }

            this.loadList()
        }

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                FertilizerTask.image(
                    this.activity,
                    it.id,
                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
                    this.resources.getDimension(R.dimen.thumb_width).toInt(),
                )
            },
            { fertilizer, image ->
                this.listView.children.forEach { feedView ->
                    val itemView = this.listView.getChildViewHolder(feedView).itemView

                    itemView.findViewById<LinearLayout>(R.id.additives).children.forEach { additiveView ->
                        if (additiveView.tag == fertilizer.id) {
                            additiveView.findViewById<ImageView>(R.id.image).setImageBitmap(image)
                        }
                    }
                }
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Feed) {
            return
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "index",
                "form",
                mapOf(
                    "task" to "feed",
                    "id" to item.id,
                    "additionalParameters" to hashMapOf(
                        "plantId" to this.fragmentsArguments["plantId"].toString(),
                    ),
                ),
                this.formLauncher,
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Feed) {
            return
        }

        view.findViewById<TextView>(R.id.added).text = "${item.added} (Tag ${item.day})"
        view.findViewById<TextView>(R.id.milliliter).text = item.milliliter.toString() + " ml"
        view.findViewById<TextView>(R.id.ph).text = item.ph.toString()

        val inflater = LayoutInflater.from(this.activity)
        val additivesView = view.findViewById<LinearLayout>(R.id.additives)
        additivesView.removeAllViews()

        item.additives.forEach {
            val additiveView = inflater.inflate(
                R.layout.grow_diary_plant_feed_additive,
                this.view?.findViewById(android.R.id.content),
                false
            )

            additiveView.tag = it.fertilizer.fertilizer.id
            additiveView.findViewById<TextView>(R.id.additive).text = it.milliliter.toString() + " " + it.fertilizer.fertilizer.formUnit + " " + it.fertilizer.fertilizer.name
            additivesView.addView(additiveView)

            val imageView = additiveView.findViewById<ImageView>(R.id.image)
            this.imageLoaderService.viewImage(it.fertilizer.fertilizer, imageView, R.drawable.ic_hemp)
        }
    }

    override fun getListRessource() = R.layout.grow_diary_plant_feed_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(FeedTask.list(
            this,
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
                "index",
                "form",
                mapOf(
                    "task" to "feed",
                    "additionalParameters" to hashMapOf(
                        "plantId" to this.fragmentsArguments["plantId"].toString(),
                    ),
                ),
                this.formLauncher,
            )
        })
    }
}