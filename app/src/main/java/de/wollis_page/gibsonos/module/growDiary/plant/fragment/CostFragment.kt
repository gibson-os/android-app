package de.wollis_page.gibsonos.module.growDiary.plant.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.enum.plant.CostType
import de.wollis_page.gibsonos.module.growDiary.index.dto.plant.Cost
import de.wollis_page.gibsonos.module.growDiary.task.ClimateControlTask
import de.wollis_page.gibsonos.module.growDiary.task.FertilizerTask
import de.wollis_page.gibsonos.module.growDiary.task.LightTask
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import de.wollis_page.gibsonos.module.growDiary.task.SeedTask
import de.wollis_page.gibsonos.module.growDiary.task.SubstrateTask
import de.wollis_page.gibsonos.service.ImageLoaderService

class CostFragment: ListFragment() {
    private lateinit var imageLoaderService: ImageLoaderService<Cost>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val thumbWidth = this.resources.getDimension(R.dimen.thumb_width).toInt()

        this.imageLoaderService = ImageLoaderService(
            this.activity,
            {
                when (it.type) {
                    CostType.SEED -> SeedTask.image(this.activity, it.foreignId, thumbWidth)
                    CostType.LIGHT -> LightTask.image(this.activity, it.foreignId, thumbWidth)
                    CostType.SUBSTRATE -> SubstrateTask.image(this.activity, it.foreignId, thumbWidth)
                    CostType.CLIMATE_CONTROL -> ClimateControlTask.image(this.activity, it.foreignId, thumbWidth)
                    CostType.FERTILIZER -> FertilizerTask.getImage(this.activity, it.foreignId, thumbWidth)
                    else -> null
                }
            },
            { fertilizer, image ->
                this.getViewByItem(fertilizer)?.findViewById<ImageView>(R.id.image)?.setImageBitmap(image)
            }
        )
    }

    override fun onClick(item: ListItemInterface) {
    }

    @SuppressLint("DefaultLocale")
    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Cost) {
            return
        }

        view.findViewById<TextView>(R.id.name).text = item.name
        view.findViewById<TextView>(R.id.totalCost).text = String.format("%.2f €", item.totalCost / 100)

        if (item.type != CostType.SUM) {
            view.findViewById<TextView>(R.id.costPerUnit).text = item.costPerUnit?.let { String.format("%.2f €", it / 100) }
            view.findViewById<TextView>(R.id.units).text = item.units?.let { String.format("%.3f", it) }
        }

        this.imageLoaderService.viewImage(item, view.findViewById(R.id.image))
    }

    override fun getListRessource() = R.layout.grow_diary_plant_cost_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(
            PlantTask.getCosts(
            this.activity,
            this.fragmentsArguments["plantId"].toString().toLong(),
        ))
    }
}