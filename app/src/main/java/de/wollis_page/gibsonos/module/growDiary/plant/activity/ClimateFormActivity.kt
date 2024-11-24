package de.wollis_page.gibsonos.module.growDiary.plant.activity

import de.wollis_page.gibsonos.activity.FormActivity
import de.wollis_page.gibsonos.dto.form.Button
import de.wollis_page.gibsonos.module.growDiary.task.PlantTask
import org.json.JSONObject

class ClimateFormActivity: FormActivity() {
    override fun buildForm() = this.loadForm {
        var climateId: Long? = this.intent.getLongExtra("cimateId", 0)

        if (climateId?.toInt() == 0) {
            climateId = null
        }

        PlantTask.getClimateForm(
            this,
            this.intent.getLongExtra("plantId", 0),
            climateId,
        )
    }

    override fun getId() = 0

    override fun afterButtonClick(
        name: String,
        formButton: Button,
        button: android.widget.Button,
        response: JSONObject
    ) {
        this.setResult(RESULT_OK)
        this.finish()
    }
}