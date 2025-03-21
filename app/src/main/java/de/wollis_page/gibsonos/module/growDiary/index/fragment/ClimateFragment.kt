package de.wollis_page.gibsonos.module.growDiary.index.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.floatingactionbutton.FloatingActionButton
import de.wollis_page.gibsonos.R
import de.wollis_page.gibsonos.dto.ListItemInterface
import de.wollis_page.gibsonos.fragment.ListFragment
import de.wollis_page.gibsonos.module.growDiary.index.dto.Climate
import de.wollis_page.gibsonos.module.growDiary.task.ClimateTask
import de.wollis_page.gibsonos.service.ActivityLauncherService

class ClimateFragment: ListFragment() {
    lateinit var formLauncher: ActivityResultLauncher<Intent>

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
    }

    override fun onClick(item: ListItemInterface) {
        if (item !is Climate) {
            return
        }

        val extras = mutableMapOf(
            "climateId" to item.id
        )

        if (this.fragmentsArguments["plantId"] != null) {
            extras["plantId"] = this.fragmentsArguments["plantId"].toString().toLong()
        }

        if (this.fragmentsArguments["setupId"] != null) {
            extras["setupId"] = this.fragmentsArguments["setupId"].toString().toLong()
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "climate",
                "form",
                extras,
                this.formLauncher,
            )
        })
    }

    override fun bind(item: ListItemInterface, view: View) {
        if (item !is Climate) {
            return
        }

        view.findViewById<TextView>(R.id.added).text = item.added
        view.findViewById<TextView>(R.id.measuringPoint).text = item.measuringPoint
        view.findViewById<TextView>(R.id.temperature).text = item.temperature.toString() + "°C"
        view.findViewById<TextView>(R.id.relativeHumidity).text = item.relativeHumidity.toString() + "%"
        view.findViewById<TextView>(R.id.airPressure).text = item.airPressure.toString()
        view.findViewById<TextView>(R.id.leafTemperature).text = item.leafTemperature.toString() + "°C"

    }

    override fun getListRessource() = R.layout.grow_diary_plant_climate_list_item

    override fun loadList(start: Long, limit: Long) = this.load {
        this.listAdapter.setListResponse(
            ClimateTask.getList(
            this.activity,
            this.fragmentsArguments["plantId"]?.toString()?.toLong(),
            this.fragmentsArguments["setupId"]?.toString()?.toLong(),
            start,
            limit,
        ))
    }

    override fun actionButton() = R.layout.base_button_add

    override fun actionView() = this.activity.findViewById<FloatingActionButton>(R.id.addButton)

    override fun actionOnClickListener() {
        val extras = mutableMapOf<String, Long>()

        if (this.fragmentsArguments["plantId"] != null) {
            extras["plantId"] = this.fragmentsArguments["plantId"].toString().toLong()
        }

        if (this.fragmentsArguments["setupId"] != null) {
            extras["setupId"] = this.fragmentsArguments["setupId"].toString().toLong()
        }

        this.runTask({
            ActivityLauncherService.startActivity(
                this.activity,
                "growDiary",
                "climate",
                "form",
                extras,
                this.formLauncher,
            )
        })
    }
}